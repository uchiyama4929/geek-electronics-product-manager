package com.example.demo.controller;

import com.example.demo.controller.web.OrderHistoryController;
import com.example.demo.entity.Manager;
import com.example.demo.entity.Order;
import com.example.demo.service.ManagerService;
import com.example.demo.service.OrderService;
import com.example.demo.test.ManagerTestFactory;
import com.example.demo.test.OrderTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderHistoryControllerTest {

    private OrderHistoryController orderHistoryController;
    private OrderService orderService;
    private ManagerService managerService;
    private Authentication authentication;
    private Model model;

    @BeforeEach
    public void setup() {
        orderService = mock(OrderService.class);
        managerService = mock(ManagerService.class);
        authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        model = mock(Model.class);
        orderHistoryController = new OrderHistoryController(orderService, managerService);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    /**
     * test case 30
     */
    @Test
    public void testIndex_normal() {
        UserDetails userDetails = mock(UserDetails.class);
        Manager manager = ManagerTestFactory.createManager(1L, 1L, 1L, 1L, "ROLE_MANAGER");
        Page<Order> orders = OrderTestFactory.createOrderPage();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(manager);
        when(orderService.findByStoreId(anyLong(), any(PageRequest.class))).thenReturn(orders);

        String viewName = orderHistoryController.index(model, 0);
        assertEquals("/order/index", viewName);
    }

    /**
     * test case 31
     */
    @Test
    public void testIndex_managerIsNull() {
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(null);

        String viewName = orderHistoryController.index(model, 0);
        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 32
     */
    @Test
    public void testIndex_authError() {
        when(authentication.getPrincipal()).thenReturn(new Object());

        String viewName = orderHistoryController.index(model, 0);
        assertEquals("redirect:/login", viewName);
    }
}
