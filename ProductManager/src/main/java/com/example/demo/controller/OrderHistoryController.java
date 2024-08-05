package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.*;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class OrderHistoryController {
    private final OrderService orderService;
    private final ManagerService managerService;

    private static final int PAGE_SIZE = 10;

    public OrderHistoryController(
            OrderService orderService,
            ManagerService managerService
    ) {
        this.orderService = orderService;
        this.managerService = managerService;
    }

    @GetMapping("/order/index")
    public String index(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page
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

        Page<Order> orders = orderService.findByStoreId(manager.getStore().getId(), PageRequest.of(page, PAGE_SIZE));

        model.addAttribute("orders", orders);
        model.addAttribute("pageName", "/order/index");
        model.addAttribute("pageObject", orders);
        return "/order/index";
    }
}
