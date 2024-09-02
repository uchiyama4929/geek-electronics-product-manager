package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.test.CategoryTestFactory;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category largeCategory;
    private Category middleCategory;
    private Category smallCategory;

    @BeforeEach
    public void setUp() {
        largeCategory = CategoryTestFactory.createCategory(1L, "Large Category", null);
        middleCategory = CategoryTestFactory.createCategory(2L, "Middle Category", largeCategory);
        smallCategory = CategoryTestFactory.createCategory(3L, "Small Category", middleCategory);
    }

    @Test
    public void testGetAllLargeCategories() {
        when(categoryRepository.findByParentIdIsNull()).thenReturn(Collections.singletonList(largeCategory));

        List<Category> result = categoryService.getAllLargeCategories();

        assertEquals(1, result.size());
        assertEquals(largeCategory, result.getFirst());
        verify(categoryRepository, times(1)).findByParentIdIsNull();
    }

    @Test
    public void testGetSubCategories() {
        when(categoryRepository.findByParentId(1L)).thenReturn(Collections.singletonList(middleCategory));

        List<Category> result = categoryService.getSubCategories(1L);

        assertEquals(1, result.size());
        assertEquals(middleCategory, result.getFirst());
        verify(categoryRepository, times(1)).findByParentId(1L);
    }

    @Test
    public void testGetSmallCategoryIds_SmallCategory() {
        List<Long> result = categoryService.getSmallCategoryIds(null, null, 3L);

        assertEquals(1, result.size());
        assertEquals(Long.valueOf(3L), result.getFirst());
    }

    @Test
    public void testGetSmallCategoryIds_MiddleCategory() {
        when(categoryRepository.findByParentId(2L)).thenReturn(Collections.singletonList(smallCategory));

        List<Long> result = categoryService.getSmallCategoryIds(null, 2L, null);

        assertEquals(1, result.size());
        assertEquals(Long.valueOf(3L), result.getFirst());
        verify(categoryRepository, times(1)).findByParentId(2L);
    }

    @Test
    public void testGetSmallCategoryIds_LargeCategory() {
        when(categoryRepository.findByParentId(1L)).thenReturn(Collections.singletonList(middleCategory));
        when(categoryRepository.findByParentId(2L)).thenReturn(Collections.singletonList(smallCategory));

        List<Long> result = categoryService.getSmallCategoryIds(1L, null, null);

        assertEquals(1, result.size());
        assertEquals(Long.valueOf(3L), result.getFirst());
        verify(categoryRepository, times(1)).findByParentId(1L);
        verify(categoryRepository, times(1)).findByParentId(2L);
    }

    @Test
    public void testFindById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(largeCategory));

        Category result = categoryService.findById(1L);

        assertEquals(largeCategory, result);
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            categoryService.findById(1L);
        });

        assertEquals("Category not found", exception.getReason());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testParseCategoryId_Valid() {
        Long result = categoryService.parseCategoryId("1");

        assertEquals(Long.valueOf(1L), result);
    }

    @Test
    public void testParseCategoryId_Invalid() {
        Long result = categoryService.parseCategoryId("invalid");

        assertNull(result);
    }

    @Test
    public void testParseCategoryId_Null() {
        Long result = categoryService.parseCategoryId(null);

        assertNull(result);
    }

    @Test
    public void testGetParentCategory() {
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(smallCategory));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(middleCategory));

        Category result = categoryService.getParentCategory(3L);

        assertEquals(middleCategory, result);
        verify(categoryRepository, times(1)).findById(3L);
        verify(categoryRepository, times(1)).findById(2L);
    }

    @Test
    public void testFindByParentIdIsNull() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> page = new PageImpl<>(Collections.singletonList(largeCategory));

        when(categoryRepository.findByParentIdIsNull(pageable)).thenReturn(page);

        Page<Category> result = categoryService.findByParentIdIsNull(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(largeCategory, result.getContent().getFirst());
        verify(categoryRepository, times(1)).findByParentIdIsNull(pageable);
    }

    @Test
    public void testFindByParentId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> page = new PageImpl<>(Collections.singletonList(middleCategory));

        when(categoryRepository.findByParentId(1L, pageable)).thenReturn(page);

        Page<Category> result = categoryService.findByParentId(1L, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(middleCategory, result.getContent().getFirst());
        verify(categoryRepository, times(1)).findByParentId(1L, pageable);
    }
}
