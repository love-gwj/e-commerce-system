package com.example.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ecommerce.entity.Product;

import java.util.List;

public interface ProductService extends IService<Product> {
    IPage<Product> getProductPage(Integer pageNum, Integer pageSize, Long categoryId, String keyword);
    List<Product> getHotProducts(Integer limit);
    Product getProductDetail(Long productId);
    void updateStock(Long productId, Integer quantity);
    
    // Elasticsearch搜索
    List<Product> searchProducts(String keyword);
    void syncProductToEs(Long productId);
    void deleteProductFromEs(Long productId);
}
