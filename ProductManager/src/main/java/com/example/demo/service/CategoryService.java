package com.example.demo.service;

import com.example.demo.entity.Category;

import java.util.List;

public interface CategoryService {
    /**
     * 親カテゴリを全件取得
     *
     * @return 親カテゴリ全件
     */
    List<Category> getAllLargeCategories();

    /**
     * 指定した親カテゴリの子カテゴリを全件取得
     *
     * @param parent_id 親カテゴリID
     * @return 子カテゴリ全件
     */
    List<Category> getSubCategories(Long parent_id);

    /**
     * 検索対象のカテゴリの一覧を取得する。
     * 大カテゴリのみが指定された場合も対象の小カテゴリの一覧を取得できる。
     *
     * @param largeCategoryId  大カテゴリID
     * @param middleCategoryId 中カテゴリID
     * @param smallCategoryId  小カテゴリID
     * @return 小カテゴリのリスト
     */
    List<Long> getSmallCategoryIds(Long largeCategoryId, Long middleCategoryId, Long smallCategoryId);

    /**
     * 入力されたカテゴリIDをLong型にキャストする
     *
     * @param categoryId カテゴリID
     * @return カテゴリID
     */
    Long parseCategoryId(String categoryId);

    /**
     * 直属の親カテゴリを取得する
     * 大分類のidを指定した場合などはnullを返す
     *
     * @param id カテゴリID
     * @return 上位のカテゴリ、またはnull
     */
    Category getParentCategory(Long id);
}
