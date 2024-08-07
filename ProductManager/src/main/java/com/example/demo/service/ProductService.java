package com.example.demo.service;

import com.example.demo.dto.ProductCategoryDTO;
import com.example.demo.dto.ProductStoreDTO;
import com.example.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    /**
     * 商品情報1件の取得
     *
     * @param product_id 商品ID
     * @return 商品情報
     */
    Product findById(Long product_id);

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

    /**
     *
     * @param storeId
     * @param keyword
     * @param categoryIds
     * @param pageable
     * @return
     */
    Page<ProductStoreDTO> getProductStoreInfo(Long storeId, String keyword, List<Long> categoryIds, Pageable pageable);

    /**
     *
     * @param id
     * @param storeId
     * @return
     */
    ProductStoreDTO findByIdAndStoreId(Long id, Long storeId);

    /**
     *
     * @return
     */
    List<ProductCategoryDTO> findAllProductStoreInfo();
}
