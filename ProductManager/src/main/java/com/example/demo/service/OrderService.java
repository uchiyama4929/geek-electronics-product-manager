package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.form.OrderForm;


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
}
