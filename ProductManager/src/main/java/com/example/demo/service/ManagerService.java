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
    Manager saveManager(ManagerForm ManagerForm);

    /**
     * パスワードの暗号化処理
     *
     * @param password 入力されたパスワード
     * @return 暗号化されたパスワード
     */
    String hashPassword(String password);

    /**
     * 暗号化後のパスワードと平文のパスワードを認証する処理
     *
     * @param password 平文のパスワード
     * @param hashedPassword 暗号化後のパスワード
     * @return 認証結果 同一の場合はtrueを返す
     */
    boolean checkPassword(String password, String hashedPassword);

    /**
     * 認証処理
     * メールアドレスとパスワードを用いてログイン可能なユーザーか判定する
     *
     * @param loginForm View変数
     * @return ログインユーザーの情報 未登録の場合はnullを返す
     */
    Manager certification(LoginForm loginForm);

    /**
     * ログイン判定
     *
     * @param session セッション
     * @return ログイン中の場合 trueを返す
     */
    boolean isLogin(HttpSession session);

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
}
