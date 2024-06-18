package com.example.demo.dto;

import com.example.demo.entity.Product;
import com.example.demo.entity.Store;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductStoreDTO(
        Long priceId,
        Long stockId,
        Product product,
        Store store,
        BigDecimal salePrice,
        Long stockQuantity,
        LocalDateTime priceCreatedAt,
        LocalDateTime stockCreatedAt,
        LocalDateTime priceUpdatedAt,
        LocalDateTime stockUpdatedAt
) {
}