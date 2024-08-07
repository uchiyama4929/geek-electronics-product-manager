package com.example.demo.repository;

import com.example.demo.dto.ProductCategoryDTO;
import com.example.demo.dto.ProductStoreDTO;
import com.example.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
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
     * @param storeId 店舗ID
     * @param keyword 検索フリーワード
     * @param categoryIds カテゴリID
     * @param pageable ページ数
     * @return 商品情報
     */
    @Query("SELECT new com.example.demo.dto.ProductStoreDTO(p, s, ps.stockQuantity, pr.salePrice) " +
            "FROM Product p " +
            "JOIN ProductStock ps ON p.id = ps.product.id " +
            "JOIN Store s ON s.id = ps.store.id " +
            "JOIN ProductPrice pr ON pr.product.id = p.id AND pr.store.id = s.id " +
            "WHERE s.id = :storeId " +
            "AND (:keyword IS NULL OR p.name LIKE %:keyword%) " +
            "AND (:categoryIds IS NULL OR p.category.id IN :categoryIds)"
            )
    Page<ProductStoreDTO> findProductStoreInfo(@Param("storeId") Long storeId,
                                               @Param("keyword") String keyword,
                                               @Param("categoryIds") List<Long> categoryIds,
                                               Pageable pageable);

    /**
     * 商品情報1件と店舗の販売価格、在庫数を取得する
     *
     * @param productId 商品ID
     * @param storeId 店舗ID
     * @return 商品情報
     */
    @Query("SELECT new com.example.demo.dto.ProductStoreDTO(p, s, ps.stockQuantity, pr.salePrice) " +
            "FROM Product p " +
            "JOIN ProductStock ps ON p.id = ps.product.id " +
            "JOIN Store s ON s.id = ps.store.id " +
            "JOIN ProductPrice pr ON pr.product.id = p.id AND pr.store.id = s.id " +
            "WHERE p.id = :productId AND s.id = :storeId")
    ProductStoreDTO findByIdAndStoreId(@Param("productId") Long productId,
                                              @Param("storeId") Long storeId);

    /**
     * 商品情報を全件取得
     *
     * @return 商品情報
     */
    @Query("SELECT new com.example.demo.dto.ProductCategoryDTO(p.name, p.description, p.costPrice, p.retailPrice, ml.name, mm.name, ms.name) FROM Product p LEFT JOIN Category ms ON ms.id = p.category.id LEFT JOIN Category mm ON mm.id = ms.parentId LEFT JOIN Category ml ON ml.id = mm.parentId")
    List<ProductCategoryDTO> findAllProductStoreInfo();
}
