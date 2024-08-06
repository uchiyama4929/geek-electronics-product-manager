package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.form.LoginForm;
import com.example.demo.form.ManagerForm;
import com.example.demo.service.PermissionService;
import com.example.demo.service.PositionService;
import com.example.demo.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.ManagerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
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
        model.addAttribute("loginForm", new LoginForm());
        return "log_in";
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
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "manager/create";
        }

        managerService.save(ManagerForm);
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
            RedirectAttributes redirectAttributes
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
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "/manager/edit";
        }


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();
        Manager currentManager;

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            currentManager = managerService.findByEmail(email);
            if (currentManager == null) {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }

        String id = managerForm.getId();
        redirectAttributes.addAttribute("id", id);

        Manager manager = managerService.save(managerForm);

        if (manager.getId().equals(currentManager.getId())) {

            UserDetails updatedUserDetails = new User(
                    manager.getEmail(),
                    manager.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    Collections.singleton(new SimpleGrantedAuthority(manager.getPermission().getName()))
            );

            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    updatedUserDetails,
                    updatedUserDetails.getPassword(),
                    updatedUserDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }

        return "redirect:/manager/detail/{id}";
    }
}