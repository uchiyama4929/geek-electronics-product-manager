package com.example.demo.service;

import com.example.demo.dto.ProductCategoryDTO;
import com.example.demo.dto.ProductStoreDTO;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(
            ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product findById(Long product_id) {
        return productRepository.findById(product_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Product> findByManufacturerId(Long manufacturerId, Pageable pageable) {
        return productRepository.findByManufacturerId(manufacturerId, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Product> findByCategoryId(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ProductStoreDTO> getProductStoreInfo(Long storeId, String keyword, List<Long> categoryIds, Pageable pageable) {
        return productRepository.findProductStoreInfo(storeId, keyword, categoryIds, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductStoreDTO findByIdAndStoreId(Long id, Long storeId) {
        return productRepository.findByIdAndStoreId(id, storeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductCategoryDTO> findAllProductStoreInfo() {
        return productRepository.findAllProductStoreInfo();
    }
}
