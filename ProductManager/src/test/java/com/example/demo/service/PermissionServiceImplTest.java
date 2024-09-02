package com.example.demo.service;

import com.example.demo.entity.Permission;
import com.example.demo.repository.PermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceImplTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionServiceImpl permissionService;

    private Permission permission1;
    private Permission permission2;

    @BeforeEach
    public void setUp() {
        permission1 = new Permission();
        permission1.setId(1L);
        permission1.setName("Permission 1");

        permission2 = new Permission();
        permission2.setId(2L);
        permission2.setName("Permission 2");
    }

    @Test
    public void testFindAll() {
        when(permissionRepository.findAll()).thenReturn(Arrays.asList(permission1, permission2));

        List<Permission> result = permissionService.findAll();

        assertEquals(2, result.size());
        assertEquals("Permission 1", result.getFirst().getName());
        assertEquals("Permission 2", result.get(1).getName());
    }
}
