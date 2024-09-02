package com.example.demo.service;

import com.example.demo.entity.Manager;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Position;
import com.example.demo.entity.Store;
import com.example.demo.form.ManagerForm;
import com.example.demo.repository.ManagerRepository;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.PositionRepository;
import com.example.demo.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ManagerServiceImplTest {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private ManagerServiceImpl managerService;

    private Manager manager;
    private ManagerForm managerForm;
    private Store store;
    private Position position;
    private Permission permission;

    @BeforeEach
    public void setUp() {
        manager = new Manager();
        manager.setId(1L);
        manager.setLastName("Test");
        manager.setFirstName("Manager");
        manager.setEmail("test@example.com");
        manager.setPhoneNumber("1234567890");
        manager.setPassword("password");
        manager.setCreatedAt(new Date());
        manager.setUpdatedAt(new Date());

        managerForm = new ManagerForm();
        managerForm.setId("1");
        managerForm.setLastName("Test");
        managerForm.setFirstName("Manager");
        managerForm.setEmail("test@example.com");
        managerForm.setPhoneNumber("1234567890");
        managerForm.setPassword("new_password");
        managerForm.setStoreId("1");
        managerForm.setPositionId("1");
        managerForm.setPermissionId("1");

        store = new Store();
        store.setId(1L);

        position = new Position();
        position.setId(1L);

        permission = new Permission();
        permission.setId(1L);
    }

    @Test
    public void testSave_Create() {
        managerForm.setId(null); // Simulate create
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        when(managerRepository.save(any(Manager.class))).thenReturn(manager);

        Manager result = managerService.save(managerForm);

        assertEquals("Test", result.getLastName());
        assertEquals("Manager", result.getFirstName());
        assertEquals("test@example.com", result.getEmail());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(managerRepository, times(1)).save(any(Manager.class));
    }

    @Test
    public void testSave_Edit() {
        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        when(managerRepository.save(any(Manager.class))).thenReturn(manager);

        Manager result = managerService.save(managerForm);

        assertEquals("Test", result.getLastName());
        assertEquals("Manager", result.getFirstName());
        assertEquals("test@example.com", result.getEmail());
        assertNotNull(result.getUpdatedAt());
        verify(managerRepository, times(1)).findById(1L);
        verify(managerRepository, times(1)).save(any(Manager.class));
    }

    @Test
    public void testSave_Edit_NotFound() {
        when(managerRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> managerService.save(managerForm));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Manager not found", exception.getReason());
        verify(managerRepository, times(1)).findById(1L);
        verify(managerRepository, never()).save(any(Manager.class));
    }

    @Test
    public void testHashPassword() {
        String rawPassword = "password";
        String hashedPassword = managerService.hashPassword(rawPassword);

        assertNotEquals(rawPassword, hashedPassword);
        assertTrue(new BCryptPasswordEncoder().matches(rawPassword, hashedPassword));
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Manager> page = new PageImpl<>(Collections.singletonList(manager));

        when(managerRepository.findAll(pageable)).thenReturn(page);

        Page<Manager> result = managerService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(manager, result.getContent().getFirst());
        verify(managerRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testFindById() {
        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));

        Manager result = managerService.findById(1L);

        assertEquals(manager, result);
        verify(managerRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(managerRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> managerService.findById(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Product not found", exception.getReason());
        verify(managerRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByEmail() {
        when(managerRepository.findByEmail("test@example.com")).thenReturn(manager);

        Manager result = managerService.findByEmail("test@example.com");

        assertEquals(manager, result);
        verify(managerRepository, times(1)).findByEmail("test@example.com");
    }
}
