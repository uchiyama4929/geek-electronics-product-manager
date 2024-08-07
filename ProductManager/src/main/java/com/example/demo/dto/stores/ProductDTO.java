package com.example.demo.dto.stores;

public record ProductDTO(
        String productName,
        Long salesPrice,
        Long stockQuantity
) {
}
