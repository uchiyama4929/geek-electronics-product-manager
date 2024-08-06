package com.example.demo.service;

import com.example.demo.entity.Manager;
import com.example.demo.form.ManagerForm;
import jakarta.servlet.http.HttpSession;
import com.example.demo.form.LoginForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManagerService {

    /**
     * 管理者情報の登録、又は更新を行う
     *
     * @param ManagerForm view変数
     * @return 登録対象の管理者情報
     */
    Manager save(ManagerForm ManagerForm);

    /**
     * パスワードの暗号化処理
     *
     * @param password 入力されたパスワード
     * @return 暗号化されたパスワード
     */
    String hashPassword(String password);

    /**
     * 管理者情報を全件取得する
     *
     * @param pageable ページネイト
     * @return 管理者情報全件
     */
    Page<Manager> findAll(Pageable pageable);

    /**
     * 指定したidの管理者情報を取得する。
     *
     * @param id ID
     * @return 管理者情報
     */
    Manager findById(Long id);

    /**
     * 指定したメールアドレスの管理者情報を取得する。
     *
     * @param email メールアドレス
     * @return 管理者情報
     */
    Manager findByEmail(String email);
}
