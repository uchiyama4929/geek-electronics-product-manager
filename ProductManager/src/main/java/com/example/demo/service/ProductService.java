package com.example.demo.service;

import com.example.demo.entity.Product;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    /**
     * 商品情報1件の取得
     *
     * @param product_id 商品ID
     * @return 商品情報
     */
    Product findById(Long product_id);
}
