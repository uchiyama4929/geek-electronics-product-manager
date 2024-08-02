package com.example.demo.service;

import com.example.demo.dto.ProductStoreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductStoreService {
    /**
     * 1店舗分の商品情報を取得
     * フリーワード検索とカテゴリの指定が可能
     * ページネイト対応済み
     *
     * @param storeId 店舗ID
     * @param keyword 検索フリーワード
     * @param categoryIds カテゴリID
     * @param pageable ページ数
     * @return 商品情報
     */
    Page<ProductStoreDTO> getProductStoreInfo(Long storeId, String keyword, List<Long> categoryIds, Pageable pageable);

    /**
     * 商品情報1件と店舗の販売価格、在庫数を取得する
     *
     * @param id 商品ID
     * @param storeId 店舗ID
     * @return 商品情報
     */
    ProductStoreDTO findByIdAndStoreId(Long id, Long storeId);
}
