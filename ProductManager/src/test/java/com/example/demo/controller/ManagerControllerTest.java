package com.example.demo.controller;

import com.example.demo.controller.web.ManagerController;
import com.example.demo.entity.Manager;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Position;
import com.example.demo.entity.Store;
import com.example.demo.form.ManagerForm;
import com.example.demo.service.ManagerService;
import com.example.demo.service.PermissionService;
import com.example.demo.service.PositionService;
import com.example.demo.service.StoreService;
import com.example.demo.test.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ManagerControllerTest {

    private ManagerController managerController;
    private ManagerService managerService;
    private StoreService storeService;
    private PositionService positionService;
    private PermissionService permissionService;
    private Model model;

    @BeforeEach
    public void setup() {
        managerService = mock(ManagerService.class);
        storeService = mock(StoreService.class);
        positionService = mock(PositionService.class);
        permissionService = mock(PermissionService.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        model = mock(Model.class);
        managerController = new ManagerController(managerService, storeService, positionService, permissionService);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    /**
     * test case 10
     */
    @Test
    public void testLogin_normal() {
        String viewName = managerController.login(model);
        assertEquals("/log_in", viewName);
    }

    /**
     * test case 11
     */
    @Test
    public void testIndex_normal() {
        Page<Manager> managers = ManagerTestFactory.createManagerPage();

        when(managerService.findAll(any(PageRequest.class))).thenReturn(managers);

        String viewName = managerController.index(model, 0);
        assertEquals("/manager/index", viewName);
    }

    /**
     * test case 12
     */
    @Test
    public void testCreateGet_normal() {
        List<Store> stores = StoreTestFactory.createStoreList();
        List<Position> positions = PositionTestFactory.createPositionList();
        List<Permission> permissions = PermissionTestFactory.createPermissionList();

        when(storeService.findAll()).thenReturn(stores);
        when(positionService.findAll()).thenReturn(positions);
        when(permissionService.findAll()).thenReturn(permissions);

        String viewName = managerController.create(model);
        assertEquals("/manager/create", viewName);
    }

    /**
     * test case 13
     */
    @Test
    public void testCreatePost_normal() {
        ManagerForm managerForm = new ManagerForm();
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = managerController.create(managerForm, bindingResult, model);
        assertEquals("redirect:/manager/index", viewName);
    }

    /**
     * test case 14
     */
    @Test
    public void testCreatePost_inValid() {
        ManagerForm managerForm = new ManagerForm();
        BindingResult bindingResult = mock(BindingResult.class);
        List<Store> stores = StoreTestFactory.createStoreList();
        List<Position> positions = PositionTestFactory.createPositionList();
        List<Permission> permissions = PermissionTestFactory.createPermissionList();

        when(bindingResult.hasErrors()).thenReturn(true);
        when(storeService.findAll()).thenReturn(stores);
        when(positionService.findAll()).thenReturn(positions);
        when(permissionService.findAll()).thenReturn(permissions);

        String viewName = managerController.create(managerForm, bindingResult, model);
        assertEquals("/manager/create", viewName);
    }

    /**
     * test case 15
     */
    @Test
    public void testDetail_normal() {
        Manager manager = ManagerTestFactory.createManager(1L, 1L, 1L, 1L, "ROLE_MANAGER");

        when(managerService.findById(1L)).thenReturn(manager);

        String viewName = managerController.detail(model, 1L);
        assertEquals("/manager/detail", viewName);
    }

    /**
     * test case 16
     */
    @Test
    public void testEditGet_normal() {
        Manager manager = ManagerTestFactory.createManager(1L, 1L, 1L, 1L, "ROLE_MANAGER");
        List<Store> stores = StoreTestFactory.createStoreList();
        List<Position> positions = PositionTestFactory.createPositionList();
        List<Permission> permissions = PermissionTestFactory.createPermissionList();

        when(managerService.findById(1L)).thenReturn(manager);
        when(storeService.findAll()).thenReturn(stores);
        when(positionService.findAll()).thenReturn(positions);
        when(permissionService.findAll()).thenReturn(permissions);

        String viewName = managerController.edit(model, 1L);
        assertEquals("/manager/edit", viewName);
    }

    /**
     * test case 17
     */
    @Test
    public void testEditPost_normal() {
        ManagerForm managerForm = new ManagerForm();
        managerForm.setId("1");
        BindingResult bindingResult = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        Manager manager = ManagerTestFactory.createManager(1L, 1L, 1L, 1L, "ROLE_MANAGER");

        Authentication authentication = ManagerTestFactory.createAuthenticationWithUserDetails(manager);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(managerService.save(any(ManagerForm.class))).thenReturn(manager);
        when(managerService.findByEmail(manager.getEmail())).thenReturn(manager);

        String viewName = managerController.edit(managerForm, bindingResult, model, redirectAttributes);
        assertEquals("redirect:/manager/detail/{id}", viewName);
    }


    @Test
    public void testEditPost_inValid() {
        ManagerForm managerForm = new ManagerForm();
        managerForm.setId("1");
        BindingResult bindingResult = mock(BindingResult.class);
        Manager manager = ManagerTestFactory.createManager(1L, 1L, 1L, 1L, "ROLE_MANAGER");
        List<Store> stores = StoreTestFactory.createStoreList();
        List<Position> positions = PositionTestFactory.createPositionList();
        List<Permission> permissions = PermissionTestFactory.createPermissionList();

        when(bindingResult.hasErrors()).thenReturn(true);
        when(managerService.findById(1L)).thenReturn(manager);
        when(storeService.findAll()).thenReturn(stores);
        when(positionService.findAll()).thenReturn(positions);
        when(permissionService.findAll()).thenReturn(permissions);

        String viewName = managerController.edit(managerForm, bindingResult, model, mock(RedirectAttributes.class));
        assertEquals("/manager/edit", viewName);
    }

    /**
     * test case 19
     */
    @Test
    public void testEditPost_managerIsNull() {
        ManagerForm managerForm = new ManagerForm();
        managerForm.setId("1");
        BindingResult bindingResult = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        Manager manager = ManagerTestFactory.createManager(1L, 1L, 1L, 1L, "ADMIN");

        Authentication authentication = ManagerTestFactory.createAuthenticationWithUserDetails(manager);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(managerService.findByEmail(manager.getEmail())).thenReturn(null);

        String viewName = managerController.edit(managerForm, bindingResult, model, redirectAttributes);
        assertEquals("redirect:/login", viewName);
    }

    /**
     * test case 20
     */
    @Test
    public void testEditPost_authError() {
        ManagerForm managerForm = new ManagerForm();
        managerForm.setId("1");
        BindingResult bindingResult = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(new Object());

        String viewName = managerController.edit(managerForm, bindingResult, model, redirectAttributes);
        assertEquals("redirect:/login", viewName);
    }
}
