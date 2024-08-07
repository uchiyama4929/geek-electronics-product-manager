package com.example.demo.dto.stores;

import java.util.Date;

public record OrderHistoryDTO(
        String productName,
        String managerName,
        Long orderQuantity,
        Long totalAmount,
        Date orderDate
) {
}
