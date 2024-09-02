package com.example.demo.controller;

import com.example.demo.controller.web.StoreController;
import com.example.demo.entity.Manager;
import com.example.demo.entity.Store;
import com.example.demo.form.StoreForm;
import com.example.demo.service.ManagerService;
import com.example.demo.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoreControllerTest {

    private StoreController storeController;
    private ManagerService managerService;
    private StoreService storeService;
    private Authentication authentication;
    private Model model;

    @BeforeEach
    public void setup() {
        managerService = mock(ManagerService.class);
        storeService = mock(StoreService.class);
        authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        model = mock(Model.class);
        storeController = new StoreController(managerService, storeService);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    /**
     * test case 52
     */
    @Test
    public void testDetail_normal() {
        Manager manager = mock(Manager.class);
        Store store = mock(Store.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(manager);
        when(manager.getStore()).thenReturn(store);
        when(store.getId()).thenReturn(1L);
        when(storeService.findById(1L)).thenReturn(store);

        String viewName = storeController.detail(model);
        assertEquals("/store/detail", viewName);
    }

    /**
     * test case 53
     */
    @Test
    public void testDetail_managerIsNull() {
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(null);

        String viewName = storeController.detail(model);
        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 54
     */
    @Test
    public void testDetail_authError() {
        when(authentication.getPrincipal()).thenReturn(new Object());

        String viewName = storeController.detail(model);
        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 55
     */
    @Test
    public void testEditGet_normal() {
        Manager manager = mock(Manager.class);
        Store store = mock(Store.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(manager);
        when(manager.getStore()).thenReturn(store);
        when(store.getId()).thenReturn(1L);
        when(storeService.findById(1L)).thenReturn(store);

        String viewName = storeController.edit(model);
        assertEquals("/store/edit", viewName);
    }

    /**
     * test case 56
     */
    @Test
    public void testEditGet_managerIsNull() {
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(null);

        String viewName = storeController.edit(model);
        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 57
     */
    @Test
    public void testEditGet_authError() {
        when(authentication.getPrincipal()).thenReturn(new Object());

        String viewName = storeController.edit(model);
        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 58
     */
    @Test
    public void testEditPost_normal() {
        Manager manager = mock(Manager.class);
        Store store = mock(Store.class);
        StoreForm storeForm = new StoreForm();
        BindingResult bindingResult = mock(BindingResult.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(manager);
        when(manager.getStore()).thenReturn(store);
        when(store.getId()).thenReturn(1L);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = storeController.edit(model, storeForm, bindingResult);
        assertEquals("redirect:/store/detail", viewName);
    }

    /**
     * test case 59
     */
    @Test
    public void testEditPost_managerIsNull() {
        StoreForm storeForm = new StoreForm();
        BindingResult bindingResult = mock(BindingResult.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(null);

        String viewName = storeController.edit(model, storeForm, bindingResult);
        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 60
     */
    @Test
    public void testEditPost_authError() {
        StoreForm storeForm = new StoreForm();
        BindingResult bindingResult = mock(BindingResult.class);

        when(authentication.getPrincipal()).thenReturn(new Object());

        String viewName = storeController.edit(model, storeForm, bindingResult);
        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 61
     */
    @Test
    public void testEditPost_inValid() {
        Manager manager = mock(Manager.class);
        Store store = mock(Store.class);
        StoreForm storeForm = new StoreForm();
        BindingResult bindingResult = mock(BindingResult.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(manager);
        when(manager.getStore()).thenReturn(store);
        when(store.getId()).thenReturn(1L);
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = storeController.edit(model, storeForm, bindingResult);
        assertEquals("/store/edit", viewName);
    }
}
