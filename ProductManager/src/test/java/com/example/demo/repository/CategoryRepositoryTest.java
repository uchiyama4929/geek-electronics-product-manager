package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.Category;

public class CategoryRepositoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByParentIdIsNull() {
        List<Category> categories = Arrays.asList(new Category(), new Category());

        when(categoryRepository.findByParentIdIsNull()).thenReturn(categories);

        List<Category> result = categoryRepository.findByParentIdIsNull();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(categoryRepository, times(1)).findByParentIdIsNull();
    }

    @Test
    public void testFindByParentIdIsNullPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(Collections.singletonList(new Category()));

        when(categoryRepository.findByParentIdIsNull(pageable)).thenReturn(categoryPage);

        Page<Category> result = categoryRepository.findByParentIdIsNull(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(categoryRepository, times(1)).findByParentIdIsNull(pageable);
    }

    @Test
    public void testFindByParentId() {
        List<Category> categories = Arrays.asList(new Category(), new Category());

        when(categoryRepository.findByParentId(1L)).thenReturn(categories);

        List<Category> result = categoryRepository.findByParentId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(categoryRepository, times(1)).findByParentId(1L);
    }

    @Test
    public void testFindByParentIdPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(Collections.singletonList(new Category()));

        when(categoryRepository.findByParentId(1L, pageable)).thenReturn(categoryPage);

        Page<Category> result = categoryRepository.findByParentId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(categoryRepository, times(1)).findByParentId(1L, pageable);
    }
}
