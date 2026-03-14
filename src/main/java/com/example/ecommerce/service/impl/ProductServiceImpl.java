package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductDocument;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.repository.ProductSearchRepository;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductSearchRepository productSearchRepository;
    private final ElasticsearchRestTemplate elasticsearchTemplate;

    @Override
    @Cacheable(value = "products", key = "#pageNum + '-' + #pageSize + '-' + #categoryId + '-' + #keyword")
    public IPage<Product> getProductPage(Integer pageNum, Integer pageSize, Long categoryId, String keyword) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(Product::getStatus, 1);
        
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Product::getName, keyword).or().like(Product::getDescription, keyword));
        }
        
        wrapper.orderByDesc(Product::getCreateTime);
        
        return page(page, wrapper);
    }

    @Override
    @Cacheable(value = "hotProducts", key = "#limit")
    public List<Product> getHotProducts(Integer limit) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1);
        wrapper.orderByDesc(Product::getSales);
        wrapper.last("LIMIT " + limit);
        return list(wrapper);
    }

    @Override
    public Product getProductDetail(Long productId) {
        Product product = getById(productId);
        if (product == null || product.getStatus() == 0) {
            throw new BusinessException("商品不存在");
        }
        return product;
    }

    @Override
    @CacheEvict(value = {"products", "hotProducts"}, allEntries = true)
    public void updateStock(Long productId, Integer quantity) {
        Product product = getById(productId);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        
        int newStock = product.getStock() + quantity;
        if (newStock < 0) {
            throw new BusinessException("库存不足");
        }
        
        product.setStock(newStock);
        updateById(product);
        
        // 同步更新ES索引
        syncProductToEs(productId);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        Criteria criteria = new Criteria("name").contains(keyword)
                .or(new Criteria("description").contains(keyword));
        CriteriaQuery query = new CriteriaQuery(criteria);
        
        SearchHits<ProductDocument> hits = elasticsearchTemplate.search(query, ProductDocument.class);
        
        return hits.stream()
                .map(hit -> {
                    Product product = new Product();
                    ProductDocument doc = hit.getContent();
                    product.setId(doc.getId());
                    product.setName(doc.getName());
                    product.setDescription(doc.getDescription());
                    product.setPrice(doc.getPrice());
                    product.setOriginalPrice(doc.getOriginalPrice());
                    product.setStock(doc.getStock());
                    product.setCategoryId(doc.getCategoryId());
                    product.setMainImage(doc.getMainImage());
                    product.setSales(doc.getSales());
                    product.setStatus(doc.getStatus());
                    return product;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncProductToEs(Long productId) {
        Product product = getById(productId);
        if (product != null && product.getStatus() == 1) {
            ProductDocument document = convertToDocument(product);
            productSearchRepository.save(document);
            log.info("商品同步到ES: {}", productId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductFromEs(Long productId) {
        productSearchRepository.deleteById(productId);
        log.info("从ES删除商品: {}", productId);
    }

    private ProductDocument convertToDocument(Product product) {
        ProductDocument document = new ProductDocument();
        document.setId(product.getId());
        document.setName(product.getName());
        document.setDescription(product.getDescription());
        document.setPrice(product.getPrice());
        document.setOriginalPrice(product.getOriginalPrice());
        document.setStock(product.getStock());
        document.setCategoryId(product.getCategoryId());
        document.setMainImage(product.getMainImage());
        document.setSales(product.getSales());
        document.setStatus(product.getStatus());
        return document;
    }
}
