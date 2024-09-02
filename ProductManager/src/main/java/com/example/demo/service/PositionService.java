package com.example.demo.service;

import com.example.demo.entity.Position;

import java.util.List;

public interface PositionService {
    /**
     * 役職情報を全権取得する
     *
     * @return 役職情報
     */
    List<Position> findAll();
}
