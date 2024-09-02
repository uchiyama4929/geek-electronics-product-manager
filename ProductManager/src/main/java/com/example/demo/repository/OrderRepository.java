package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * 店舗の発注履歴を取得
     * ページネイト対応
     *
     * @param storeId 店舗ID
     * @param pageable ページネイト
     * @return 発注履歴
     */
    Page<Order> findByStoreId(Long storeId, Pageable pageable);
}
