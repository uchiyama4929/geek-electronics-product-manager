package com.example.demo.controller;

import com.example.demo.dto.ProductStoreDTO;
import com.example.demo.entity.*;
import com.example.demo.form.CategoryForm;
import com.example.demo.form.LoginForm;
import com.example.demo.form.ManagerCreateForm;
import com.example.demo.service.PermissionService;
import com.example.demo.service.PositionService;
import com.example.demo.service.StoreService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.ManagerService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;


@Controller
public class ManagerController {
    private final ManagerService managerService;
    private final StoreService storeService;
    private final PositionService positionService;
    private final PermissionService permissionService;

    private static final int PAGE_SIZE = 10;

    public ManagerController(
            ManagerService managerService,
            StoreService storeService,
            PositionService positionService,
            PermissionService permissionService) {
        this.managerService = managerService;
        this.storeService = storeService;
        this.positionService = positionService;
        this.permissionService = permissionService;
    }

    /**
     * ログイン画面初期表示
     *
     * @param model   view変数
     * @param request リクエスト
     * @return ログイン画面
     */
    @GetMapping("/login")
    public String contact(Model model, HttpServletRequest request) {

        // ログインチェック
        HttpSession session = request.getSession();
        if (managerService.isLogin(session)) {
            return "redirect:/product/index";
        }

        model.addAttribute("loginForm", new LoginForm());

        return "log_in";
    }

    /**
     * ログイン機能
     *
     * @param loginForm   view変数
     * @param errorResult バリデーションエラーデータ
     * @param request     リクエスト
     * @return お問い合わせ一覧画面(バリデーションエラー時はログイン画面)
     */
    @PostMapping("/login")
    public String certification(
            @Validated @ModelAttribute("loginForm") LoginForm loginForm,
            BindingResult errorResult,
            HttpServletRequest request
    ) {

        // ログインチェック
        HttpSession session = request.getSession();
        if (managerService.isLogin(session)) {
            return "redirect:/manager/contacts";
        }

        // バリデーション
        if (errorResult.hasErrors()) {
            return "log_in";
        }

        // ログインの確認
        Manager manager = managerService.certification(loginForm);
        if (manager == null) {
            return "log_in";
        }

        //ログイン情報をセッションに保持
        session.setAttribute("manager", manager);

        return "redirect:/product/index";
    }

    @GetMapping("/manager/index")
    public String index(
            Model model,
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {

        Page<Manager> managers = managerService.findAll(PageRequest.of(page, PAGE_SIZE));

        model.addAttribute("managers", managers);
        model.addAttribute("pageObject", managers);
        return "/manager/index";
    }

    @GetMapping("/manager/create")
    public String create(Model model) {
        List<Store> stores = storeService.findAll();
        List<Position> positions = positionService.findAll();
        List<Permission> permissions = permissionService.findAll();

        model.addAttribute("stores", stores);
        model.addAttribute("positions", positions);
        model.addAttribute("permissions", permissions);
        model.addAttribute("selectedStoreId", "0");
        model.addAttribute("selectedPositionId", "0");
        model.addAttribute("selectedPermissionId", "0");
        model.addAttribute("ManagerCreateForm", new ManagerCreateForm());

        return "/manager/create";
    }

    @PostMapping("/manager/create")
    public String create(
            @Valid @ModelAttribute("categoryForm") ManagerCreateForm managerCreateForm,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            List<Store> stores = storeService.findAll();
            List<Position> positions = positionService.findAll();
            List<Permission> permissions = permissionService.findAll();

            model.addAttribute("stores", stores);
            model.addAttribute("positions", positions);
            model.addAttribute("permissions", permissions);
            model.addAttribute("selectedStoreId", managerCreateForm.getStoreId());
            model.addAttribute("selectedPositionId", managerCreateForm.getPositionId());
            model.addAttribute("selectedPermissionId", managerCreateForm.getPermissionId());
            model.addAttribute("ManagerCreateForm", managerCreateForm);
            return "manager/create";
        }

        Manager manager = managerService.saveManager(managerCreateForm);
        return "redirect:/manager/index";
    }

    @GetMapping("/manager/detail/{id}")
    public String detail(
            Model model,
            HttpServletRequest request,
            @PathVariable(name = "id") Long id
    ) {
        Manager manager = managerService.findById(id);
        model.addAttribute("manager", manager);
        return "/manager/detail";
    }
}