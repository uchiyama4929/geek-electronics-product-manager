package com.example.demo.controller;

import com.example.demo.entity.Manager;
import com.example.demo.entity.Order;
import com.example.demo.form.OrderForm;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService
    ) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<Order> createOrder(
            @Validated @RequestBody OrderForm orderForm,
            BindingResult bindingResult,
            HttpSession session
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Object managerObject = session.getAttribute("manager");
        if (!(managerObject instanceof Manager managerSession)) {
            return ResponseEntity.badRequest().build();
        }

        Long productId = orderForm.getProductId();
        String orderQuantity = orderForm.getQuantity();
        Order order = orderService.createOrder(orderQuantity, productId, managerSession.getId(), managerSession.getStore().getId());
        return ResponseEntity.ok(order);
    }
}
