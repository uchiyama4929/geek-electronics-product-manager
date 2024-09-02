package com.example.demo.service;

import com.example.demo.entity.Manufacturer;
import com.example.demo.form.ManufacturerForm;
import com.example.demo.repository.ManufacturerRepository;
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

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ManufacturerServiceImplTest {

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private ManufacturerServiceImpl manufacturerService;

    private Manufacturer manufacturer;
    private ManufacturerForm manufacturerForm;

    @BeforeEach
    public void setUp() {
        manufacturer = new Manufacturer();
        manufacturer.setId(1L);
        manufacturer.setName("Test Manufacturer");
        manufacturer.setCreatedAt(new Date());
        manufacturer.setUpdatedAt(new Date());

        manufacturerForm = new ManufacturerForm();
        manufacturerForm.setId("1");
        manufacturerForm.setName("Updated Manufacturer");
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Manufacturer> page = new PageImpl<>(Collections.singletonList(manufacturer));

        when(manufacturerRepository.findAll(pageable)).thenReturn(page);

        Page<Manufacturer> result = manufacturerService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(manufacturer, result.getContent().getFirst());
        verify(manufacturerRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testSave_Create() {
        manufacturerForm.setId(null);

        when(manufacturerRepository.save(any(Manufacturer.class))).thenReturn(manufacturer);

        Manufacturer result = manufacturerService.save(manufacturerForm);

        assertEquals("Updated Manufacturer", result.getName());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(manufacturerRepository, times(1)).save(any(Manufacturer.class));
    }

    @Test
    public void testSave_Edit() {
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(manufacturer));
        when(manufacturerRepository.save(any(Manufacturer.class))).thenReturn(manufacturer);

        Manufacturer result = manufacturerService.save(manufacturerForm);

        assertEquals("Updated Manufacturer", result.getName());
        assertNotNull(result.getUpdatedAt());
        verify(manufacturerRepository, times(1)).findById(1L);
        verify(manufacturerRepository, times(1)).save(any(Manufacturer.class));
    }

    @Test
    public void testSave_Edit_NotFound() {
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> manufacturerService.save(manufacturerForm));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Manufacturer not found", exception.getReason());
        verify(manufacturerRepository, times(1)).findById(1L);
        verify(manufacturerRepository, never()).save(any(Manufacturer.class));
    }

    @Test
    public void testFindById() {
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(manufacturer));

        Manufacturer result = manufacturerService.findById(1L);

        assertEquals(manufacturer, result);
        verify(manufacturerRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> manufacturerService.findById(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Product not found", exception.getReason());
        verify(manufacturerRepository, times(1)).findById(1L);
    }

    @Test
    public void testDelete() {
        doNothing().when(manufacturerRepository).deleteById(1L);

        manufacturerService.delete(1L);

        verify(manufacturerRepository, times(1)).deleteById(1L);
    }
}
