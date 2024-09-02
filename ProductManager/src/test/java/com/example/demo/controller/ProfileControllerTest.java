package com.example.demo.controller;

import com.example.demo.controller.web.ProfileController;
import com.example.demo.entity.Manager;
import com.example.demo.form.ManagerForm;
import com.example.demo.service.ManagerService;
import com.example.demo.test.ManagerTestFactory;
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

public class ProfileControllerTest {

    private ProfileController profileController;
    private ManagerService managerService;
    private Authentication authentication;
    private Model model;

    @BeforeEach
    public void setup() {
        managerService = mock(ManagerService.class);
        authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        model = mock(Model.class);
        profileController = new ProfileController(managerService);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    /**
     * test case 41
     */
    @Test
    public void testDetail_normal() {
        Manager manager = mock(Manager.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(manager);

        String viewName = profileController.detail(model);
        assertEquals("/profile/detail", viewName);
    }

    /**
     * test case 42
     */
    @Test
    public void testDetail_managerIsNull() {
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(null);

        String viewName = profileController.detail(model);
        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 43
     */
    @Test
    public void testDetail_authError() {
        when(authentication.getPrincipal()).thenReturn(new Object());

        String viewName = profileController.detail(model);
        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 44
     */
    @Test
    public void testEditGet_normal() {
        Manager manager = ManagerTestFactory.createManager(1L, 2L, 3L, 4L, "ADMIN");
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(manager);

        String viewName = profileController.edit(model);
        assertEquals("/profile/edit", viewName);
    }

    /**
     * test case 45
     */
    @Test
    public void testEditGet_managerIsNull() {
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(null);

        String viewName = profileController.edit(model);
        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 46
     */
    @Test
    public void testEditGet_authError() {
        when(authentication.getPrincipal()).thenReturn(new Object());

        String viewName = profileController.edit(model);
        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 47
     */
    @Test
    public void testEditPost_normal() {
        Manager manager = ManagerTestFactory.createManager(1L, 2L, 3L, 4L, "ROLE_MANAGER");
        ManagerForm managerForm = new ManagerForm();
        BindingResult bindingResult = mock(BindingResult.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(manager);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(managerService.save(any(ManagerForm.class))).thenReturn(manager);

        String viewName = profileController.edit(model, managerForm, bindingResult);
        assertEquals("redirect:/profile/detail", viewName);
    }

    /**
     * test case 48
     */
    @Test
    public void testEditPost_WhenManagerIsNull() {
        ManagerForm managerForm = new ManagerForm();
        BindingResult bindingResult = mock(BindingResult.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(null);

        String viewName = profileController.edit(model, managerForm, bindingResult);
        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 49
     */
    @Test
    public void testEditPost_AuthError() {
        ManagerForm managerForm = new ManagerForm();
        BindingResult bindingResult = mock(BindingResult.class);

        when(authentication.getPrincipal()).thenReturn(new Object());

        String viewName = profileController.edit(model, managerForm, bindingResult);
        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 50
     */
    @Test
    public void testEditPost_inValid() {
        Manager manager = mock(Manager.class);
        ManagerForm managerForm = new ManagerForm();
        BindingResult bindingResult = mock(BindingResult.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(managerService.findByEmail("user@example.com")).thenReturn(manager);
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = profileController.edit(model, managerForm, bindingResult);
        assertEquals("/profile/edit", viewName);
    }
}
