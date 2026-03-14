package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.mapper.CategoryMapper;
import com.example.ecommerce.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> getCategoryTree() {
        List<Category> allCategories = list();
        return buildTree(allCategories, 0L);
    }

    private List<Category> buildTree(List<Category> categories, Long parentId) {
        return categories.stream()
            .filter(c -> c.getParentId().equals(parentId))
            .collect(Collectors.toList());
    }

    @Override
    public List<Category> getByParentId(Long parentId) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, parentId);
        wrapper.orderByAsc(Category::getSort);
        return list(wrapper);
    }
}
