package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.test.CategoryTestFactory;
import com.example.demo.test.ManufacturerTestFactory;
import com.example.demo.test.ProductTestFactory;
import com.example.demo.test.StoreTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.entity.*;

public class ProductStockRepositoryTest {

    @Mock
    private ProductStockRepository productStockRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByProductIdAndStoreId() {
        Category parentCategory = CategoryTestFactory.createCategory(10L, "Parent Category", null);
        Category category = CategoryTestFactory.createCategory(1L, "category", parentCategory);
        Manufacturer manufacturer = ManufacturerTestFactory.createManufacturer(1L, "Manufacturer");
        Product product = ProductTestFactory.createProduct(1L, "テストアイテム", category, manufacturer, 100L, 200L);
        Store store = StoreTestFactory.createStore(1L, "Geek電機", "東京都渋谷区");
        ProductStock productStock = ProductTestFactory.createProductStock(100L, product, store);

        when(productStockRepository.findByProductIdAndStoreId(1L, 1L)).thenReturn(productStock);

        ProductStock foundProductStock = productStockRepository.findByProductIdAndStoreId(1L, 1L);

        assertNotNull(foundProductStock);
        assertEquals(100L, foundProductStock.getStockQuantity());
        assertEquals(product.getId(), foundProductStock.getProduct().getId());
        assertEquals(store.getId(), foundProductStock.getStore().getId());

        verify(productStockRepository, times(1)).findByProductIdAndStoreId(1L, 1L);
    }
}
