package com.example.demo.controller.web;

import jakarta.validation.Valid;
import com.example.demo.entity.*;
import com.example.demo.service.*;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import com.example.demo.form.ManufacturerForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/manufacturer")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    private static final int PAGE_SIZE = 10;
    private final ProductService productService;

    public ManufacturerController(
            ManufacturerService manufacturerService,
            ProductService productService
    ) {
        this.manufacturerService = manufacturerService;
        this.productService = productService;
    }

    @GetMapping("/index")
    public String index(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Page<Manufacturer> manufacturers = manufacturerService.findAll(PageRequest.of(page, PAGE_SIZE));

        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("pageName", "/manufacturer/index");
        model.addAttribute("pageObject", manufacturers);
        return "/manufacturer/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("ManufacturerForm", new ManufacturerForm());
        return "/manufacturer/create";
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("manufacturerForm") ManufacturerForm manufacturerForm,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("ManufacturerForm", manufacturerForm);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "manufacturer/create";
        }

        manufacturerService.save(manufacturerForm);
        return "redirect:/manufacturer/index";
    }

    @GetMapping("/detail/{id}")
    public String detail(
            Model model,
            @PathVariable(name = "id") Long id,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Manufacturer manufacturer = manufacturerService.findById(id);
        Page<Product> products = productService.findByManufacturerId(id, PageRequest.of(page, PAGE_SIZE));
        model.addAttribute("manufacturer", manufacturer);
        model.addAttribute("products", products);
        return "/manufacturer/detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            Model model,
            @PathVariable(name = "id") Long id
    ) {
        Manufacturer manufacturer = manufacturerService.findById(id);

        ManufacturerForm manufacturerForm = new ManufacturerForm();
        manufacturerForm.setId(String.valueOf(manufacturer.getId()));
        manufacturerForm.setName(manufacturer.getName());

        model.addAttribute("manufacturerForm", manufacturerForm);
        model.addAttribute("manufacturer", manufacturer);

        return "/manufacturer/edit";
    }


    @PostMapping("/edit/{id}")
    public String edit(
            @Valid @ModelAttribute("manufacturerForm") ManufacturerForm manufacturerForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            @PathVariable(name = "id") Long id
    ) {
        if (bindingResult.hasErrors()) {
            Manufacturer manufacturer = manufacturerService.findById(Long.valueOf(manufacturerForm.getId()));
            model.addAttribute("manufacturer", manufacturer);
            model.addAttribute("ManufacturerForm", manufacturerForm);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "/manufacturer/edit";
        }
        manufacturerService.save(manufacturerForm);
        redirectAttributes.addAttribute("id", id);

        return "redirect:/manufacturer/detail/{id}";
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable(name = "id") Long id
    ) {
        manufacturerService.delete(id);
        return "redirect:/manufacturer/index";
    }
}
