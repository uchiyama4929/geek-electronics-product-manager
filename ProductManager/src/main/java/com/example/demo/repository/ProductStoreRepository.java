package com.example.demo.repository;

import com.example.demo.dto.ProductCategoryDTO;
import com.example.demo.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductStoreRepository extends JpaRepository<ProductPrice, Long>, ProductStoreRepositoryCustom {
    @Query("SELECT new com.example.demo.dto.ProductCategoryDTO(p.name, p.description, p.costPrice, p.retailPrice, ml.name, mm.name, ms.name) FROM Product p LEFT JOIN Category ms ON ms.id = p.category.id LEFT JOIN Category mm ON mm.id = ms.parentId LEFT JOIN Category ml ON ml.id = mm.parentId")
    List<ProductCategoryDTO> findAllProductStoreInfo();
}
