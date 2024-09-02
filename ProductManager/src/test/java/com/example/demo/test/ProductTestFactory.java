package com.example.demo.test;

import com.example.demo.dto.ProductStoreDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.Manufacturer;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductStock;
import com.example.demo.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductTestFactory {

    public static Product createProduct(Long productId, Category category) {
        Product product = mock(Product.class);
        when(product.getId()).thenReturn(productId);
        when(product.getCategory()).thenReturn(category);
        return product;
    }

    public static Product createProduct(Long id, Manufacturer manufacturer) {
        Product product = mock(Product.class);
        when(product.getId()).thenReturn(id);
        when(product.getManufacturer()).thenReturn(manufacturer);
        return product;
    }

    public static Product createProduct(Long id, String name, Category category, Manufacturer manufacturer, Long costPrice, Long retailPrice) {
        Product product = new Product();
        product.setId(id);
        product.setCategory(category);
        product.setManufacturer(manufacturer);
        product.setName(name);
        product.setDescription("テストデータ");
        product.setImage("");
        product.setCostPrice(costPrice);
        product.setRetailPrice(retailPrice);
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());
        return product;
    }

    public static ProductStoreDTO createProductStoreDTO(Product product) {
        ProductStoreDTO productStoreDTO = mock(ProductStoreDTO.class);
        when(productStoreDTO.product()).thenReturn(product);
        return productStoreDTO;
    }

    public static Page<Product> createProductPage() {
        return new PageImpl<>(Collections.emptyList());
    }

    public static Page<ProductStoreDTO> createProductStoreDTOPage() {
        return new PageImpl<>(Collections.emptyList());
    }

    public static ProductStock createProductStock(Long stockQuantity, Product product, Store store) {
        ProductStock productStock = new ProductStock();
        productStock.setProduct(product);
        productStock.setStore(store);
        productStock.setStockQuantity(stockQuantity);
        productStock.setCreatedAt(new Date());
        productStock.setUpdatedAt(new Date());
        return productStock;
    }
}
