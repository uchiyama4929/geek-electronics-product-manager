package com.example.demo.service;

import com.example.demo.dto.ProductStoreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductStoreService {
    Page<ProductStoreDTO> getProductStoreInfo(Long storeId, String keyword, List<Long> categoryIds, Pageable pageable);
}
