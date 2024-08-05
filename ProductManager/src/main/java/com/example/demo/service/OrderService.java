package com.example.demo.service;

import com.example.demo.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    /**
     * 注文内容を保存する、また店舗在庫を追加する。
     *
     * @param orderQuantity  注文数
     * @param orderProductId 商品ID
     * @param managerId      管理者ID（注文者）
     * @param storeId        店舗ID
     * @return 注文情報
     */
    Order createOrder(String orderQuantity, Long orderProductId, Long managerId, Long storeId);

    /**
     * 店舗に紐づく発注情報を全件取得
     * ページネイト対応
     *
     * @param storeId  店舗ID
     * @param pageable ページネイト
     * @return 発注履歴
     */
    Page<Order> findByStoreId(Long storeId, Pageable pageable);
}
