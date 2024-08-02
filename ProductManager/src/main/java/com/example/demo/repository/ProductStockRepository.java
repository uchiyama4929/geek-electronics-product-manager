package com.example.demo.repository;

import com.example.demo.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    ProductStock findByProductId(Long productId);
}
