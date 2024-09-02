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

import com.example.demo.dto.ProductCategoryDTO;
import com.example.demo.dto.ProductStoreDTO;
import com.example.demo.entity.Product;
import com.example.demo.entity.Store;

public class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByManufacturerId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(new Product()));

        when(productRepository.findByManufacturerId(1L, pageable)).thenReturn(productPage);

        Page<Product> result = productRepository.findByManufacturerId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(productRepository, times(1)).findByManufacturerId(1L, pageable);
    }

    @Test
    public void testFindByCategoryId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(new Product()));

        when(productRepository.findByCategoryId(1L, pageable)).thenReturn(productPage);

        Page<Product> result = productRepository.findByCategoryId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(productRepository, times(1)).findByCategoryId(1L, pageable);
    }

    @Test
    public void testFindProductStoreInfo() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductStoreDTO> productStorePage = new PageImpl<>(Collections.singletonList(
                new ProductStoreDTO(new Product(), new Store(), 100L, 200L)
        ));

        when(productRepository.findProductStoreInfo(1L, "keyword", Arrays.asList(1L, 2L, 3L), pageable))
                .thenReturn(productStorePage);

        Page<ProductStoreDTO> result = productRepository.findProductStoreInfo(1L, "keyword", Arrays.asList(1L, 2L, 3L), pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(productRepository, times(1)).findProductStoreInfo(1L, "keyword", Arrays.asList(1L, 2L, 3L), pageable);
    }

    @Test
    public void testFindByIdAndStoreId() {
        ProductStoreDTO productStoreDTO = new ProductStoreDTO(new Product(), new Store(), 100L, 200L);

        when(productRepository.findByIdAndStoreId(1L, 1L)).thenReturn(productStoreDTO);

        ProductStoreDTO result = productRepository.findByIdAndStoreId(1L, 1L);

        assertNotNull(result);
        assertEquals(100L, result.stockQuantity());
        assertEquals(200L, result.salePrice());

        verify(productRepository, times(1)).findByIdAndStoreId(1L, 1L);
    }

    @Test
    public void testFindAllProductStoreInfo() {
        List<ProductCategoryDTO> productCategoryDTOs = Collections.singletonList(
                new ProductCategoryDTO("name", "description", 100L, 200L, "largeCategory", "middleCategory", "smallCategory")
        );

        when(productRepository.findAllProductStoreInfo()).thenReturn(productCategoryDTOs);

        List<ProductCategoryDTO> result = productRepository.findAllProductStoreInfo();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("name", result.getFirst().productName());

        verify(productRepository, times(1)).findAllProductStoreInfo();
    }
}
