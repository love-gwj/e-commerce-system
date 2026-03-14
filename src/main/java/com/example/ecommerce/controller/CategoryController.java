package com.example.ecommerce.controller;

import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/tree")
    public Result<List<Category>> tree() {
        List<Category> categories = categoryService.getCategoryTree();
        return Result.success(categories);
    }

    @GetMapping("/list")
    public Result<List<Category>> list(@RequestParam(required = false) Long parentId) {
        List<Category> categories;
        if (parentId == null) {
            categories = categoryService.getByParentId(0L);
        } else {
            categories = categoryService.getByParentId(parentId);
        }
        return Result.success(categories);
    }
}
