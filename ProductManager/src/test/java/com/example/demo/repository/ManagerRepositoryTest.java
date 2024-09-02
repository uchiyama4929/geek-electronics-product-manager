package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.entity.Manager;

public class ManagerRepositoryTest {

    @Mock
    private ManagerRepository managerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByEmail() {
        Manager manager = new Manager();
        manager.setEmail("test@example.com");

        when(managerRepository.findByEmail("test@example.com")).thenReturn(manager);

        Manager result = managerRepository.findByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());

        verify(managerRepository, times(1)).findByEmail("test@example.com");
    }
}
