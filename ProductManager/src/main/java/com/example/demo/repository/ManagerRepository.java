package com.example.demo.repository;

import com.example.demo.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    /**
     * メールアドレスで管理者１件を検索
     *
     * @param email メールアドレス
     * @return 管理者情報
     */
    Manager findByEmail(String email);
}