package com.example.demo.controller.web;

import com.example.demo.dto.ProductStoreDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.Manager;
import com.example.demo.form.CategoryForm;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ManagerService;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ManagerService managerService;
    private static final int PAGE_SIZE = 12;

    public ProductController(
            ProductService productService,
            CategoryService categoryService,
            ManagerService managerService
    ) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.managerService = managerService;
    }

    @GetMapping("/index")
    public String index(
            @Valid @ModelAttribute("categoryForm") CategoryForm categoryForm,
            BindingResult bindingResult,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();
        Long storeId;

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            Manager manager = managerService.findByEmail(email);
            if (manager == null) {
                return "redirect:/login";
            }
            storeId = manager.getStore().getId();
        } else {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            Page<ProductStoreDTO> productStoreDtoList = productService.getProductStoreInfo(storeId, null, null, PageRequest.of(page, PAGE_SIZE));
            List<Category> largeCategories = categoryService.getAllLargeCategories();

            model.addAttribute("productStoreDtoList", productStoreDtoList);
            model.addAttribute("pageObject", productStoreDtoList);
            model.addAttribute("pageName", "/product/index");
            model.addAttribute("largeCategories", largeCategories);
            return "product/index";
        }

        Long inputLargeCategory = categoryService.parseCategoryId(categoryForm.getLargeCategory());
        Long inputMiddleCategory = categoryService.parseCategoryId(categoryForm.getMiddleCategory());
        Long inputSmallCategory = categoryService.parseCategoryId(categoryForm.getSmallCategory());
        List<Long> targetCategory = categoryService.getSmallCategoryIds(inputLargeCategory, inputMiddleCategory, inputSmallCategory);

        String keyword = categoryForm.getKeyword();
        Page<ProductStoreDTO> productStoreDtoList = productService.getProductStoreInfo(storeId, keyword, targetCategory, PageRequest.of(page, PAGE_SIZE));

        List<Category> largeCategories = categoryService.getAllLargeCategories();
        List<Category> middleCategories = inputLargeCategory != null ? categoryService.getSubCategories(inputLargeCategory) : Collections.emptyList();
        List<Category> smallCategories = inputMiddleCategory != null ? categoryService.getSubCategories(inputMiddleCategory) : Collections.emptyList();

        model.addAttribute("largeCategories", largeCategories);
        model.addAttribute("middleCategories", middleCategories);
        model.addAttribute("smallCategories", smallCategories);
        model.addAttribute("productStoreDtoList", productStoreDtoList);
        model.addAttribute("pageObject", productStoreDtoList);
        model.addAttribute("pageName", "/product/index");
        model.addAttribute("largeCategoryId", inputLargeCategory);
        model.addAttribute("middleCategoryId", inputMiddleCategory);
        model.addAttribute("smallCategoryId", inputSmallCategory);
        model.addAttribute("keyword", keyword);

        return "product/index";
    }

    @GetMapping("/detail/{id}")
    public String detail(
            Model model,
            @PathVariable(name = "id") Long id
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();
        Long storeId;

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            Manager manager = managerService.findByEmail(email);
            if (manager == null) {
                return "redirect:/login";
            }
            storeId = manager.getStore().getId();
        } else {
            return "redirect:/login";
        }

        ProductStoreDTO productStoreDto = productService.findByIdAndStoreId(id, storeId);
        if (productStoreDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        Category middleCategory = categoryService.getParentCategory(productStoreDto.product().getCategory().getId());
        Category largeCategory = categoryService.getParentCategory(middleCategory.getId());

        model.addAttribute("productStoreDto", productStoreDto);
        model.addAttribute("middleCategory", middleCategory);
        model.addAttribute("largeCategory", largeCategory);
        model.addAttribute("productId", id);
        return "product/detail";
    }
}

