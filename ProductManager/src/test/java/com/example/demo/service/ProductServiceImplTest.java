package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.ProductCategoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ProductStoreDTO;
import com.example.demo.repository.ProductRepository;
import com.example.demo.entity.Product;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_ProductExists() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.findById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
    }

    @Test
    public void testFindById_ProductNotFound() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> productService.findById(productId));

        String expectedMessage = "Product not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindByManufacturerId() {
        Long manufacturerId = 1L;
        Pageable pageable = mock(Pageable.class);
        Page<Product> productPage = new PageImpl<>(Collections.emptyList());

        when(productRepository.findByManufacturerId(manufacturerId, pageable)).thenReturn(productPage);

        Page<Product> result = productService.findByManufacturerId(manufacturerId, pageable);

        assertNotNull(result);
        assertEquals(productPage, result);
    }

    @Test
    public void testFindByCategoryId() {
        Long categoryId = 1L;
        Pageable pageable = mock(Pageable.class);
        Page<Product> productPage = new PageImpl<>(Collections.emptyList());

        when(productRepository.findByCategoryId(categoryId, pageable)).thenReturn(productPage);

        Page<Product> result = productService.findByCategoryId(categoryId, pageable);

        assertNotNull(result);
        assertEquals(productPage, result);
    }

    @Test
    public void testGetProductStoreInfo() {
        Long storeId = 1L;
        String keyword = "test";
        List<Long> categoryIds = Collections.singletonList(1L);
        Pageable pageable = mock(Pageable.class);
        Page<ProductStoreDTO> productStoreDTOPage = new PageImpl<>(Collections.emptyList());

        when(productRepository.findProductStoreInfo(storeId, keyword, categoryIds, pageable)).thenReturn(productStoreDTOPage);

        Page<ProductStoreDTO> result = productService.getProductStoreInfo(storeId, keyword, categoryIds, pageable);

        assertNotNull(result);
        assertEquals(productStoreDTOPage, result);
    }

    @Test
    public void testFindByIdAndStoreId() {
        Long productId = 1L;
        Long storeId = 1L;
        ProductStoreDTO productStoreDTO = new ProductStoreDTO(null, null, 1000L, 10L);

        when(productRepository.findByIdAndStoreId(productId, storeId)).thenReturn(productStoreDTO);

        ProductStoreDTO result = productService.findByIdAndStoreId(productId, storeId);

        assertNotNull(result);
        assertEquals(productStoreDTO, result);
    }

    @Test
    public void testFindAllProductStoreInfo() {
        List<ProductCategoryDTO> productCategoryDTOList = Collections.emptyList();

        when(productRepository.findAllProductStoreInfo()).thenReturn(productCategoryDTOList);

        List<ProductCategoryDTO> result = productService.findAllProductStoreInfo();

        assertNotNull(result);
        assertEquals(productCategoryDTOList, result);
    }
}
