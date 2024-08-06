package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private static final int PAGE_SIZE = 10;

    public CategoryController(
            CategoryService categoryService,
            ProductService productService
    ) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/getSubCategories/{parentId}")
    @ResponseBody
    public List<Category> getSubCategories(@PathVariable Long parentId) {
        return categoryService.getSubCategories(parentId);
    }

    @GetMapping("/large_index")
    public String largeIndex(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Page<Category> largeCategories = categoryService.findByParentIdIsNull(PageRequest.of(page, PAGE_SIZE));
        model.addAttribute("largeCategories", largeCategories);
        model.addAttribute("pageName", "/category/large_index");
        model.addAttribute("pageObject", largeCategories);
        return "category/large_index";
    }

    @GetMapping("/middle_index/{parent_id}")
    public String middleIndex(
            Model model,
            @PathVariable(name = "parent_id") Long parentId,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Category largeCategory = categoryService.findById(parentId);
        Page<Category> middleCategories = categoryService.findByParentId(parentId, PageRequest.of(page, PAGE_SIZE));
        model.addAttribute("largeCategory", largeCategory);
        model.addAttribute("middleCategories", middleCategories);
        model.addAttribute("pageName", "/category/middle_index");
        model.addAttribute("pageObject", middleCategories);

        return "category/middle_index";
    }

    @GetMapping("/small_index/{parent_id}")
    public String smallIndex(
            Model model,
            @PathVariable(name = "parent_id") Long parentId,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Category middleCategory = categoryService.findById(parentId);
        Page<Category> smallCategories = categoryService.findByParentId(parentId, PageRequest.of(page, PAGE_SIZE));
        model.addAttribute("middleCategory", middleCategory);
        model.addAttribute("smallCategories", smallCategories);
        model.addAttribute("pageName", "/category/small_index");
        model.addAttribute("pageObject", smallCategories);

        return "category/small_index";
    }

    @GetMapping("/small_detail/{id}")
    public String smallDetail(
            Model model,
            @PathVariable(name = "id") Long id,
            @RequestParam(value = "page", defaultValue = "0") int page

    ) {
        Category category = categoryService.findById(id);
        Page<Product> products = productService.findByCategoryId(category.getId(), PageRequest.of(page, PAGE_SIZE));
        model.addAttribute("category", category);
        model.addAttribute("pageName", "/category/small_detail");
        model.addAttribute("products", products);
        model.addAttribute("pageObject", products);

        return "category/small_detail";
    }
}
