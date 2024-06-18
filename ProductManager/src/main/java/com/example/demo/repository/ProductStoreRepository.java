package com.example.demo.repository;

import com.example.demo.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStoreRepository extends JpaRepository<ProductPrice, Long>, ProductStoreRepositoryCustom {
}
