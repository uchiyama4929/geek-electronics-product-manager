package com.example.demo.repository;

import com.example.demo.dto.ProductStoreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductStoreRepositoryCustom {
    Page<ProductStoreDTO> findProductStoreInfo(Long storeId, String keyword, List<Long> categoryIds, Pageable pageable);

    Optional<ProductStoreDTO> findByIdAndStoreId(Long id, Long storeId);
}
