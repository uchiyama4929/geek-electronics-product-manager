package com.example.demo.service;

import com.example.demo.entity.Manufacturer;
import com.example.demo.form.ManufacturerForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManufacturerService {
    /**
     * メーカー情報の全件取得
     * ページネイト対応
     *
     * @param pageable ページネイト
     * @return メーカー情報
     */
    Page<Manufacturer> findAll(Pageable pageable);

    /**
     * メーカー登録、編集
     *
     * @param manufacturerForm メーカー情報
     * @return 登録後のメーカー情報
     */
    Manufacturer save(ManufacturerForm manufacturerForm);

    /**
     * メーカ情報1件の取得
     *
     * @param id メーカーID
     * @return メーカー情報
     */
    Manufacturer findById(Long id);

    /**
     * メーカーの削除処理
     *
     * @param id メーカーID
     */
    void delete(Long id);
}