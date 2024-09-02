package com.example.demo.service;

import com.example.demo.entity.Manager;
import com.example.demo.entity.Permission;
import com.example.demo.repository.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private Manager manager;

    @BeforeEach
    public void setUp() {
        manager = new Manager();
        manager.setEmail("test@example.com");
        manager.setPassword("password");
        Permission permission = new Permission();
        permission.setId(1L);
        manager.setPermission(permission);
    }

    @Test
    public void testLoadUserByUsername_Success() {
        when(managerRepository.findByEmail("test@example.com")).thenReturn(manager);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")));

        verify(managerRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    public void testLoadUserByUsername_ManagerNotFound() {
        when(managerRepository.findByEmail("test@example.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("test@example.com");
        });

        verify(managerRepository, times(1)).findByEmail("test@example.com");
    }
}
