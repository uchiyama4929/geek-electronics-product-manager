package com.example.demo.repository;

import com.example.demo.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    /**
     * 店舗ごとの商品の在庫数情報を取得
     *
     * @param productId 商品ID
     * @param storeId 店舗ID
     * @return 在庫数情報
     */
    ProductStock findByProductIdAndStoreId(Long productId, Long storeId);
}
