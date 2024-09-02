package com.example.demo.controller;

import com.example.demo.controller.web.ProductController;
import com.example.demo.dto.ProductStoreDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.Manager;
import com.example.demo.entity.Product;
import com.example.demo.form.CategoryForm;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ManagerService;
import com.example.demo.service.ProductService;
import com.example.demo.test.CategoryTestFactory;
import com.example.demo.test.ManagerTestFactory;
import com.example.demo.test.ProductTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    private ProductController productController;
    private ProductService productService;
    private CategoryService categoryService;
    private ManagerService managerService;
    private Authentication authentication;
    private Model model;

    @BeforeEach
    public void setup() {
        productService = mock(ProductService.class);
        categoryService = mock(CategoryService.class);
        managerService = mock(ManagerService.class);
        authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        model = mock(Model.class);
        productController = new ProductController(productService, categoryService, managerService);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    /**
     * test case 33
     */
    @Test
    public void testIndex_normal() {
        CategoryForm categoryForm = new CategoryForm();
        BindingResult bindingResult = mock(BindingResult.class);
        UserDetails userDetails = mock(UserDetails.class);
        Manager manager = ManagerTestFactory.createManager(1L, 1L, 1L, 1L, "ROLE_MANAGER");
        Page<ProductStoreDTO> productStoreDtoList = ProductTestFactory.createProductStoreDTOPage();
        List<Category> largeCategories = CategoryTestFactory.createCategoryList();
        List<Category> middleCategories = CategoryTestFactory.createCategoryList();
        List<Category> smallCategories = CategoryTestFactory.createCategoryList();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(manager);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(categoryService.parseCategoryId(anyString())).thenReturn(null);
        when(categoryService.getSmallCategoryIds(any(), any(), any())).thenReturn(Collections.emptyList());
        when(productService.getProductStoreInfo(anyLong(), anyString(), anyList(), any(PageRequest.class))).thenReturn(productStoreDtoList);
        when(categoryService.getAllLargeCategories()).thenReturn(largeCategories);
        when(categoryService.getSubCategories(anyLong())).thenReturn(middleCategories, smallCategories);

        String viewName = productController.index(categoryForm, bindingResult, 0, model);
        assertEquals("product/index", viewName);
    }

    /**
     * test case 34
     */
    @Test
    public void testIndex_managerIsNull() {
        CategoryForm categoryForm = new CategoryForm();
        BindingResult bindingResult = mock(BindingResult.class);
        UserDetails userDetails = mock(UserDetails.class);
        Manager manager = ManagerTestFactory.createManager(1L, 1L, 1L, 1L, "ROLE_MANAGER");
        Page<ProductStoreDTO> productStoreDtoList = ProductTestFactory.createProductStoreDTOPage();
        List<Category> largeCategories = CategoryTestFactory.createCategoryList();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(manager);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(productService.getProductStoreInfo(anyLong(), anyString(), anyList(), any(PageRequest.class))).thenReturn(productStoreDtoList);
        when(categoryService.getAllLargeCategories()).thenReturn(largeCategories);

        String viewName = productController.index(categoryForm, bindingResult, 0, model);
        assertEquals("product/index", viewName);
    }

    /**
     * test case 35
     */
    @Test
    public void testIndex_authError() {
        when(authentication.getPrincipal()).thenReturn(new Object());
        String viewName = productController.index(new CategoryForm(), mock(BindingResult.class), 0, model);
        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 36
     */
    @Test
    public void testIndex_inValid() {
        CategoryForm categoryForm = new CategoryForm();
        BindingResult bindingResult = mock(BindingResult.class);
        UserDetails userDetails = mock(UserDetails.class);
        Manager manager = ManagerTestFactory.createManager(1L, 1L, 1L, 1L, "ADMIN");
        Page<ProductStoreDTO> productStoreDtoList = ProductTestFactory.createProductStoreDTOPage();
        List<Category> largeCategories = CategoryTestFactory.createCategoryList();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(manager);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(productService.getProductStoreInfo(anyLong(), anyString(), anyList(), any(PageRequest.class))).thenReturn(productStoreDtoList);
        when(categoryService.getAllLargeCategories()).thenReturn(largeCategories);

        String viewName = productController.index(categoryForm, bindingResult, 0, model);

        assertEquals("product/index", viewName);
    }

    /**
     * test case 37
     */
    @Test
    public void testDetail_normal() {
        UserDetails userDetails = mock(UserDetails.class);
        Manager manager = ManagerTestFactory.createManager(1L, 1L, 1L, 1L, "ROLE_MANAGER");

        Category largeCategory = CategoryTestFactory.createCategory(3L, "Large Category", null);
        Category middleCategory = CategoryTestFactory.createCategory(2L, "Middle Category", largeCategory);
        Category category = CategoryTestFactory.createCategory(1L, "Category", middleCategory);

        Product product = ProductTestFactory.createProduct(3L, category);
        ProductStoreDTO productStoreDto = ProductTestFactory.createProductStoreDTO(product);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(manager);
        when(productService.findByIdAndStoreId(anyLong(), anyLong())).thenReturn(productStoreDto);
        when(categoryService.getParentCategory(category.getId())).thenReturn(middleCategory);
        when(categoryService.getParentCategory(middleCategory.getId())).thenReturn(largeCategory);

        String viewName = productController.detail(model, 1L);
        assertEquals("product/detail", viewName);
    }

    /**
     * test case 38
     */
    @Test
    public void testDetail_managerIsNull() {
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(null);

        String viewName = productController.detail(model, 1L);

        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 39
     */
    @Test
    public void testDetail_authError() {
        when(authentication.getPrincipal()).thenReturn(new Object());

        String viewName = productController.detail(model, 1L);

        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 40
     */
    @Test
    public void testDetail_productNotFound() {
        UserDetails userDetails = mock(UserDetails.class);
        Manager manager = ManagerTestFactory.createManager(1L, 1L, 1L, 1L, "ADMIN");

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(manager);
        when(productService.findByIdAndStoreId(1L, manager.getStore().getId())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            productController.detail(model, 1L);
        });
    }
}
