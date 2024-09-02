package com.example.demo.service;

import com.example.demo.entity.Permission;

import java.util.List;

public interface PermissionService {
    /**
     * 権限情報を全権取得する
     *
     * @return 権限情報
     */
    List<Permission> findAll();
}
