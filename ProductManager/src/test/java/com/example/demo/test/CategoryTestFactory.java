package com.example.demo.test;

import com.example.demo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CategoryTestFactory {

    public static Category createCategory(Long id, String name, Category parent) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setParent(parent);
        category.setCreatedAt(new Date());
        category.setUpdatedAt(new Date());
        return category;
    }

    public static Page<Category> createCategoryPage() {
        return new PageImpl<>(Collections.emptyList());
    }

    public static List<Category> createCategoryList() {
        return Collections.emptyList();
    }
}
