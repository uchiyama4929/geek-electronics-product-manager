package com.example.demo.dto;

import com.example.demo.entity.Product;
import com.example.demo.entity.Store;

public record ProductStoreDTO(
        Product product,
        Store store,
        Long stockQuantity,
        Long salePrice
) {
}
