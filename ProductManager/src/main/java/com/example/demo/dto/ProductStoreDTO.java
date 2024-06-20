package com.example.demo.dto;

import com.example.demo.entity.Product;
import com.example.demo.entity.Store;

import java.math.BigDecimal;
import java.util.Date;

public record ProductStoreDTO(
        Long priceId,
        Long stockId,
        Product product,
        Store store,
        BigDecimal salePrice,
        Long stockQuantity,
        Date priceCreatedAt,
        Date stockCreatedAt,
        Date priceUpdatedAt,
        Date stockUpdatedAt
) {
}