package com.example.demo.dto;

public record ProductCategoryDTO (
        String productName,
        String productDescription,
        Long costPrice,
        Long suggestedRetailPrice,
        String largeCategory,
        String mediumCategory,
        String smallCategory
) {
}
