package com.example.demo.service;

import com.example.demo.entity.Store;

import java.util.List;

public interface StoreService {
    /**
     * 店舗情報の全件取得
     *
     * @return 店舗情報
     */
    List<Store> findAll();

    /**
     * 店舗情報1件の取得
     *
     * @param id 店舗ID
     * @return 店舗情報
     */
    Store findById(Long id);
}
