package com.example.demo.repository;

import com.example.demo.dto.ProductStoreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductStoreRepositoryCustom {
    Page<ProductStoreDTO> findProductStoreInfo(Long storeId, String keyword, List<Long> categoryIds, Pageable pageable);
}
