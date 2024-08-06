package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Category> getAllLargeCategories() {
        return categoryRepository.findByParentIdIsNull();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Category> getSubCategories(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> getSmallCategoryIds(Long largeCategoryId, Long middleCategoryId, Long smallCategoryId) {
        List<Long> categoryIds = new ArrayList<>();

        if (smallCategoryId != null) {
            categoryIds.add(smallCategoryId);
            return categoryIds;
        }

        if (middleCategoryId != null) {
            List<Category> smallCategories = getSubCategories(middleCategoryId);
            for (Category category : smallCategories) {
                categoryIds.add(category.getId());
            }
            return categoryIds;
        }

        if (largeCategoryId != null) {
            List<Category> middleCategories = getSubCategories(largeCategoryId);
            for (Category middleCategory : middleCategories) {
                List<Category> smallCategories = getSubCategories(middleCategory.getId());
                for (Category smallCategory : smallCategories) {
                    categoryIds.add(smallCategory.getId());
                }
            }
        }

        return categoryIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long parseCategoryId(String categoryId) {
        if (categoryId == null || categoryId.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.valueOf(categoryId);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category getParentCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null && category.getParentId() != null) {
            return categoryRepository.findById(category.getParentId()).orElse(null);
        }
        return null;
    }

}
