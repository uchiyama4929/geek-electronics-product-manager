package com.example.demo.controller;

import com.example.demo.entity.Manager;
import com.example.demo.form.ManagerForm;
import com.example.demo.service.ManagerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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

import java.util.Collections;

import static java.lang.String.valueOf;


@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ManagerService managerService;

    public ProfileController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/detail")
    public String detail(
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();
        Manager manager;

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            manager = managerService.findByEmail(email);
            if (manager == null) {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }

        model.addAttribute("manager", manager);
        return "/profile/detail";
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();
        Manager manager;

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            manager = managerService.findByEmail(email);
            if (manager == null) {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }

        ManagerForm managerForm = new ManagerForm();
        managerForm.setStoreId(valueOf(manager.getStore().getId()));
        managerForm.setPositionId(valueOf(manager.getPosition().getId()));
        managerForm.setPermissionId(valueOf(manager.getPermission().getId()));
        managerForm.setLastName(manager.getLastName());
        managerForm.setFirstName(manager.getFirstName());
        managerForm.setEmail(manager.getEmail());
        managerForm.setPhoneNumber(manager.getPhoneNumber());

        model.addAttribute("manager", manager);
        model.addAttribute("ManagerForm", managerForm);

        return "/profile/edit";
    }

    @PostMapping("/edit")
    public String edit(
            Model model,
            @Valid @ModelAttribute("managerForm") ManagerForm managerForm,
            BindingResult bindingResult,
            HttpSession session
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();
        Manager manager;

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            manager = managerService.findByEmail(email);
            if (manager == null) {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("manager", manager);
            model.addAttribute("ManagerForm", managerForm);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "/profile/edit";
        }

        //profileの場合は編集不可のためログイン情報で据え置き
        managerForm.setId(valueOf(manager.getId()));
        managerForm.setStoreId(valueOf(manager.getStore().getId()));
        managerForm.setPositionId(valueOf(manager.getPosition().getId()));
        managerForm.setPermissionId(valueOf(manager.getPermission().getId()));

        Manager updatedManager = managerService.saveManager(managerForm);


        UserDetails updatedUserDetails = new User(
                updatedManager.getEmail(),
                updatedManager.getPassword(),
                true,
                true,
                true,
                true,
                Collections.singleton(new SimpleGrantedAuthority(updatedManager.getPermission().getName()))
        );

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedUserDetails,
                updatedUserDetails.getPassword(),
                updatedUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "redirect:/profile/detail";
    }
}
