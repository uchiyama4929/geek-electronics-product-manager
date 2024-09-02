package com.example.demo.dto.stores;

import java.util.List;

public record StoreDTO(
        String storeName,
        String address,
        List<ProductDTO> products,
        List<OrderHistoryDTO> orderHistory
) {
}
