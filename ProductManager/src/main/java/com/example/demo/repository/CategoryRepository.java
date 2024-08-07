package com.example.demo.repository;

import com.example.demo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * 大カテゴリを全件取得
     *
     * @return 大カテゴリ
     */
    List<Category> findByParentIdIsNull();

    Page<Category> findByParentIdIsNull(Pageable pageable);

    /**
     * 指定したidの小カテゴリを全件取得
     *
     * @param parentId 上位の分類のカテゴリID
     * @return 直下のカテゴリ一覧
     */
    List<Category> findByParentId(Long parentId);

    Page<Category> findByParentId(Long parentId, Pageable pageable);
}
