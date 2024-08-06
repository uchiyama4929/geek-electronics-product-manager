package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * メーカーIDに紐づく商品を全件取得
     * ページネイト対応
     *
     * @param manufacturerId メーカーID
     * @param pageable ページネイト
     * @return 商品情報
     */
    Page<Product> findByManufacturerId(Long manufacturerId, Pageable pageable);

    /**
     * カテゴリIDに紐づく商品を全件取得
     * ページネイト対応
     *
     * @param categoryId カテゴリID
     * @param pageable ページネイト
     * @return 商品情報
     */
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
}
