package com.example.demo.controller;

import jakarta.validation.Valid;
import org.springframework.ui.Model;
import com.example.demo.entity.Store;
import com.example.demo.entity.Manager;
import com.example.demo.form.StoreForm;
import com.example.demo.service.StoreService;
import com.example.demo.service.ManagerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/store")
public class StoreController {

    private final ManagerService managerService;
    private final StoreService storeService;

    public StoreController(
            ManagerService managerService,
            StoreService storeService
    ) {
        this.managerService = managerService;
        this.storeService = storeService;
    }

    @GetMapping("/detail")
    public String detail(
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();
        Manager manager;

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            manager = managerService.findByEmail(email);
            if (manager == null) {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }

        Store store = storeService.findById(manager.getStore().getId());

        model.addAttribute("store", store);
        return "/store/detail";
    }

    @GetMapping("/edit")
    public String edit(
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();
        Manager manager;

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            manager = managerService.findByEmail(email);
            if (manager == null) {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }

        Store store = storeService.findById(manager.getStore().getId());

        StoreForm storeForm = new StoreForm();
        storeForm.setId(store.getId());
        storeForm.setName(store.getName());
        storeForm.setAddress(store.getAddress());

        model.addAttribute("StoreForm", storeForm);

        return "/store/edit";
    }

    @PostMapping("/edit")
    public String edit(
            Model model,
            @Valid @ModelAttribute("storeForm") StoreForm storeForm,
            BindingResult bindingResult
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();
        Manager manager;

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            manager = managerService.findByEmail(email);
            if (manager == null) {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("storeForm", storeForm);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "/profile/edit";
        }

        storeForm.setId(manager.getStore().getId());
        storeService.save(storeForm);

        return "redirect:/store/detail";
    }

}
