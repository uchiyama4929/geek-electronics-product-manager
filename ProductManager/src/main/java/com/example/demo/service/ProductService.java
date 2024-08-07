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
     * @param pageable       ページネイト
     * @return 商品情報
     */
    Page<Product> findByManufacturerId(Long manufacturerId, Pageable pageable);

    /**
     * カテゴリIDに紐づく商品を全件取得
     * ページネイト対応
     *
     * @param categoryId カテゴリID
     * @param pageable   ページネイト
     * @return 商品情報
     */
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * 1店舗分の商品情報を取得
     * フリーワード検索とカテゴリの指定が可能
     * ページネイト対応済み
     *
     * @param storeId     店舗ID
     * @param keyword     検索フリーワード
     * @param categoryIds カテゴリID
     * @param pageable    ページ数
     * @return 商品情報
     */
    Page<ProductStoreDTO> getProductStoreInfo(Long storeId, String keyword, List<Long> categoryIds, Pageable pageable);

    /**
     * 商品情報1件と店舗の販売価格、在庫数を取得する
     *
     * @param id 商品ID
     * @param storeId   店舗ID
     * @return 商品情報
     */
    ProductStoreDTO findByIdAndStoreId(Long id, Long storeId);

    /**
     * 商品情報を全件取得
     *
     * @return 商品情報
     */
    List<ProductCategoryDTO> findAllProductStoreInfo();
}
