package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.form.LoginForm;
import com.example.demo.form.ManagerForm;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import static java.lang.String.valueOf;


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
    public String login(Model model, HttpServletRequest request) {

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
     * @param session     セッション
     * @return お問い合わせ一覧画面(バリデーションエラー時はログイン画面)
     */
    @PostMapping("/login")
    public String certification(
            @Validated @ModelAttribute("loginForm") LoginForm loginForm,
            BindingResult errorResult,
            HttpSession session
    ) {
        if (errorResult.hasErrors()) {
            return "log_in";
        }

        Manager manager = managerService.certification(loginForm);
        if (manager == null) {
            return "log_in";
        }

        session.setAttribute("manager", manager);

        return "redirect:/product/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/manager/index")
    public String index(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Page<Manager> managers = managerService.findAll(PageRequest.of(page, PAGE_SIZE));

        model.addAttribute("managers", managers);
        model.addAttribute("pageName", "/manager/index");
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
        model.addAttribute("ManagerForm", new ManagerForm());

        return "/manager/create";
    }

    @PostMapping("/manager/create")
    public String create(
            @Valid @ModelAttribute("ManagerForm") ManagerForm ManagerForm,
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
            model.addAttribute("selectedStoreId", ManagerForm.getStoreId());
            model.addAttribute("selectedPositionId", ManagerForm.getPositionId());
            model.addAttribute("selectedPermissionId", ManagerForm.getPermissionId());
            model.addAttribute("ManagerForm", ManagerForm);
            return "manager/create";
        }

        Manager manager = managerService.saveManager(ManagerForm);
        return "redirect:/manager/index";
    }

    @GetMapping("/manager/detail/{id}")
    public String detail(
            Model model,
            @PathVariable(name = "id") Long id
    ) {
        Manager manager = managerService.findById(id);
        model.addAttribute("manager", manager);
        return "/manager/detail";
    }

    @GetMapping("/manager/edit/{id}")
    public String edit(
            Model model,
            @PathVariable(name = "id") Long id
    ) {
        Manager manager = managerService.findById(id);
        List<Store> stores = storeService.findAll();
        List<Position> positions = positionService.findAll();
        List<Permission> permissions = permissionService.findAll();

        ManagerForm managerForm = new ManagerForm();
        managerForm.setId(valueOf(manager.getId()));
        managerForm.setStoreId(valueOf(manager.getStore().getId()));
        managerForm.setPositionId(valueOf(manager.getPosition().getId()));
        managerForm.setPermissionId(valueOf(manager.getPermission().getId()));
        managerForm.setLastName(manager.getLastName());
        managerForm.setFirstName(manager.getFirstName());
        managerForm.setEmail(manager.getEmail());
        managerForm.setPhoneNumber(manager.getPhoneNumber());

        model.addAttribute("stores", stores);
        model.addAttribute("manager", manager);
        model.addAttribute("positions", positions);
        model.addAttribute("permissions", permissions);
        model.addAttribute("ManagerForm", managerForm);

        return "/manager/edit";
    }


    @PostMapping("/manager/edit")
    public String edit(
            @Valid @ModelAttribute("managerForm") ManagerForm managerForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ){
        if (bindingResult.hasErrors()) {
            List<Store> stores = storeService.findAll();
            List<Position> positions = positionService.findAll();
            List<Permission> permissions = permissionService.findAll();

            Manager manager = managerService.findById(Long.valueOf(managerForm.getId()));

            model.addAttribute("stores", stores);
            model.addAttribute("manager", manager);
            model.addAttribute("positions", positions);
            model.addAttribute("permissions", permissions);
            model.addAttribute("ManagerForm", managerForm);
            return "/manager/edit";
        }
        String id = managerForm.getId();
        redirectAttributes.addAttribute("id", id);

        Manager manager = managerService.saveManager(managerForm);

        Object managerObject = session.getAttribute("manager");

        if (!(managerObject instanceof Manager sessionManager)) {
            return "redirect:/login";
        }

        if (manager.getId().equals(sessionManager.getId())) {
            session.setAttribute("manager", manager);
        }

        return "redirect:/manager/detail/{id}";
    }
}