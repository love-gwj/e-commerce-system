package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public Result<IPage<Product>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        IPage<Product> page = productService.getProductPage(pageNum, pageSize, categoryId, keyword);
        return Result.success(page);
    }

    @GetMapping("/hot")
    public Result<List<Product>> hot(@RequestParam(defaultValue = "10") Integer limit) {
        List<Product> products = productService.getHotProducts(limit);
        return Result.success(products);
    }

    @GetMapping("/{id}")
    public Result<Product> detail(@PathVariable Long id) {
        Product product = productService.getProductDetail(id);
        return Result.success(product);
    }

    @GetMapping("/search")
    public Result<List<Product>> search(@RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        return Result.success(products);
    }

    @PostMapping("/sync/{id}")
    public Result<?> syncToEs(@PathVariable Long id) {
        productService.syncProductToEs(id);
        return Result.success();
    }
}
