package com.example.demo.service;

import com.example.demo.dto.ProductStoreDTO;
import com.example.demo.repository.ProductStoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductStoreServiceImpl implements ProductStoreService {

    private final ProductStoreRepository productStoreRepository;

    public ProductStoreServiceImpl(ProductStoreRepository productStoreRepository) {
        this.productStoreRepository = productStoreRepository;
    }

    public Page<ProductStoreDTO> getProductStoreInfo(Long storeId, String keyword, List<Long> categoryIds, Pageable pageable) {
        return productStoreRepository.findProductStoreInfo(storeId, keyword, categoryIds, pageable);
    }
}
