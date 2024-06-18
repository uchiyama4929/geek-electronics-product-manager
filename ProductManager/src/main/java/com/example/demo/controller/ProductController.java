package com.example.demo.controller;

import com.example.demo.dto.ProductStoreDTO;
import com.example.demo.entity.Category;
import com.example.demo.form.CategoryForm;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductStoreService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductStoreService productStoreService;
    private final CategoryService categoryService;
    private static final int PAGE_SIZE = 3;

    public ProductController(
            ProductStoreService productStoreService,
            CategoryService categoryService
    ) {
        this.productStoreService = productStoreService;
        this.categoryService = categoryService;
    }

    @GetMapping("/index")
    public String product(
            @Valid @ModelAttribute("categoryForm") CategoryForm categoryForm,
            BindingResult bindingResult,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model
    ) {
        Long storeId = 1L;
        if (bindingResult.hasErrors()) {
            Page<ProductStoreDTO> productStoreDtoList = productStoreService.getProductStoreInfo(storeId, null, null, PageRequest.of(page, PAGE_SIZE));
            List<Category> largeCategories = categoryService.getAllLargeCategories();

            model.addAttribute("productStoreDtoList", productStoreDtoList);
            model.addAttribute("largeCategories", largeCategories);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "product/index";
        }

        Long inputLargeCategory = categoryService.parseCategoryId(categoryForm.getLargeCategory());
        Long inputMiddleCategory = categoryService.parseCategoryId(categoryForm.getMiddleCategory());
        Long inputSmallCategory = categoryService.parseCategoryId(categoryForm.getSmallCategory());
        List<Long> targetCategory = categoryService.getSmallCategoryIds(inputLargeCategory, inputMiddleCategory, inputSmallCategory);

        String keyword = categoryForm.getKeyword();
        Page<ProductStoreDTO> productStoreDtoList = productStoreService.getProductStoreInfo(storeId, keyword, targetCategory, PageRequest.of(page, PAGE_SIZE));

        List<Category> largeCategories = categoryService.getAllLargeCategories();
        List<Category> middleCategories = inputLargeCategory != null ? categoryService.getSubCategories(inputLargeCategory) : Collections.emptyList();
        List<Category> smallCategories = inputMiddleCategory != null ? categoryService.getSubCategories(inputMiddleCategory) : Collections.emptyList();

        model.addAttribute("largeCategories", largeCategories);
        model.addAttribute("middleCategories", middleCategories);
        model.addAttribute("smallCategories", smallCategories);
        model.addAttribute("productStoreDtoList", productStoreDtoList);
        model.addAttribute("largeCategoryId", inputLargeCategory);
        model.addAttribute("middleCategoryId", inputMiddleCategory);
        model.addAttribute("smallCategoryId", inputSmallCategory);

        return "product/index";
    }
}
