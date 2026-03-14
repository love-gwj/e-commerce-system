package com.example.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ecommerce.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {
    List<Category> getCategoryTree();
    List<Category> getByParentId(Long parentId);
}
