package com.example.demo.filter;

import com.example.demo.service.ManagerService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class AuthenticationFilter implements Filter {

    private final ManagerService managerService;

    public AuthenticationFilter(ManagerService managerService) {
        this.managerService = managerService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // フィルターの初期化
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();

        // 認証が必要ないパスを指定（ログイン画面や静的リソースなど）
        String loginURI = httpRequest.getContextPath() + "/login";
        String cssURI = httpRequest.getContextPath() + "/css/";
        String jsURI = httpRequest.getContextPath() + "/js/";

        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);
        boolean isCssRequest = httpRequest.getRequestURI().startsWith(cssURI);
        boolean isJsRequest = httpRequest.getRequestURI().startsWith(jsURI);
        boolean isLoggedIn = managerService.isLogin(session);

        if (isLoggedIn || isLoginRequest || isCssRequest || isJsRequest) {
            chain.doFilter(request, response); // ユーザーがログインしているか、リクエストがログインページ、または静的リソースなら次のフィルターを実行
        } else {
            httpResponse.sendRedirect(loginURI); // それ以外の場合はログインページへリダイレクト
        }
    }

    @Override
    public void destroy() {
        // フィルターの破棄
    }
}
