package com.example.demo.controller;

import com.example.demo.controller.web.CategoryController;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import com.example.demo.test.CategoryTestFactory;
import com.example.demo.test.ProductTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CategoryControllerTest {

    private CategoryController categoryController;
    private CategoryService categoryService;
    private ProductService productService;
    private Model model;

    @BeforeEach
    public void setup() {
        categoryService = mock(CategoryService.class);
        productService = mock(ProductService.class);
        model = mock(Model.class);
        categoryController = new CategoryController(categoryService, productService);
    }

    /**
     * test case 1
     */
    @Test
    public void testGetSubCategories_normal() {
        Long parentId = 1L;
        List<Category> subCategories = CategoryTestFactory.createCategoryList();

        when(categoryService.getSubCategories(parentId)).thenReturn(subCategories);

        List<Category> result = categoryController.getSubCategories(parentId);
        assertEquals(subCategories, result);
    }

    /**
     * test case 2
     */
    @Test
    public void testLargeIndex_normal() {
        Page<Category> categories = CategoryTestFactory.createCategoryPage();

        when(categoryService.findByParentIdIsNull(any(PageRequest.class))).thenReturn(categories);

        String viewName = categoryController.largeIndex(model, 0);
        assertEquals("/category/large_index", viewName);
    }

    /**
     * test case 3
     */
    @Test
    public void testMiddleIndex_normal() {
        Long parentId = 1L;
        Category largeCategory = CategoryTestFactory.createCategory(parentId, "Large Category", null);
        Page<Category> categories = CategoryTestFactory.createCategoryPage();

        when(categoryService.findById(parentId)).thenReturn(largeCategory);
        when(categoryService.findByParentId(eq(parentId), any(PageRequest.class))).thenReturn(categories);

        String viewName = categoryController.middleIndex(model, parentId, 0);
        assertEquals("/category/middle_index", viewName);
    }

    /**
     * test case 4
     */
    @Test
    public void testSmallIndex_normal() {
        Long largeCategoryId = 2L;
        Long middleCategoryId = 1L;

        Category largeCategory = CategoryTestFactory.createCategory(largeCategoryId, "Large Category", null);

        Category middleCategory = CategoryTestFactory.createCategory(middleCategoryId, "Middle Category", largeCategory);

        Page<Category> categories = CategoryTestFactory.createCategoryPage();

        when(categoryService.findById(middleCategoryId)).thenReturn(middleCategory);
        when(categoryService.findByParentId(eq(middleCategoryId), any(PageRequest.class))).thenReturn(categories);

        String viewName = categoryController.smallIndex(model, middleCategoryId, 0);
        assertEquals("/category/small_index", viewName);
    }

    /**
     * test case 5
     */
    @Test
    public void testSmallDetail_ReturnsCorrectView() {
        Long largeCategoryId = 2L;
        Long middleCategoryId = 3L;
        Long smallCategoryId = 1L;

        Category largeCategory = CategoryTestFactory.createCategory(largeCategoryId, "Large Category", null);

        Category middleCategory = CategoryTestFactory.createCategory(middleCategoryId, "Middle Category", largeCategory);

        Category smallCategory = CategoryTestFactory.createCategory(smallCategoryId, "Small Category", middleCategory);

        Page<Product> products = ProductTestFactory.createProductPage();

        when(categoryService.findById(smallCategoryId)).thenReturn(smallCategory);
        when(productService.findByCategoryId(eq(smallCategoryId), any(PageRequest.class))).thenReturn(products);

        String viewName = categoryController.smallDetail(model, smallCategoryId, 0);
        assertEquals("/category/small_detail", viewName);
    }
}
