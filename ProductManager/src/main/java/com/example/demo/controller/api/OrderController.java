package com.example.demo.controller.api;

import com.example.demo.entity.Manager;
import com.example.demo.entity.Order;
import com.example.demo.form.OrderForm;
import com.example.demo.service.ManagerService;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final ManagerService managerService;

    public OrderController(
            OrderService orderService,
            ManagerService managerService
    ) {
        this.orderService = orderService;
        this.managerService = managerService;
    }

    @PostMapping("/api/orders")
    @Transactional
    public ResponseEntity<Order> createOrder(
            @Validated @RequestBody OrderForm orderForm,
            BindingResult bindingResult,
            HttpSession session
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();
        Long managerId;
        Long storeId;

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            Manager manager = managerService.findByEmail(email);
            if (manager == null) {
                return ResponseEntity.badRequest().build();
            }
            managerId = manager.getId();
            storeId = manager.getStore().getId();
        } else {
            return ResponseEntity.badRequest().build();
        }

        Long productId = orderForm.getProductId();
        String orderQuantity = orderForm.getQuantity();
        Order order = orderService.createOrder(orderQuantity, productId, managerId, storeId);
        return ResponseEntity.ok(order);
    }
}
