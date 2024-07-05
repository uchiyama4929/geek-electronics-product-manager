package com.example.demo.controller;

import com.example.demo.entity.Manager;
import com.example.demo.form.ManagerForm;
import com.example.demo.service.ManagerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
            Model model,
            HttpSession session
    ) {
        Object manager = session.getAttribute("manager");
        model.addAttribute("manager", manager);
        return "/profile/detail";
    }

    @GetMapping("/edit")
    public String edit(Model model, HttpSession session) {
        Object managerObject = session.getAttribute("manager");

        if (managerObject instanceof Manager manager) {
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
        } else {
            return "redirect:/login";
        }

        return "/profile/edit";
    }

    @PostMapping("/edit")
    public String edit(
            Model model,
            @Valid @ModelAttribute("managerForm") ManagerForm managerForm,
            BindingResult bindingResult,
            HttpSession session
    ) {
        Object managerObject = session.getAttribute("manager");

        if (bindingResult.hasErrors()) {
            model.addAttribute("manager", managerObject);
            model.addAttribute("ManagerForm", managerForm);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "/profile/edit";
        }

        if (!(managerObject instanceof Manager managerSession)) {
            return "redirect:/login";
        }

        //profileの場合は編集不可のためsession値を据え置き
        managerForm.setId(valueOf(managerSession.getId()));
        managerForm.setStoreId(valueOf(managerSession.getStore().getId()));
        managerForm.setPositionId(valueOf(managerSession.getPosition().getId()));
        managerForm.setPermissionId(valueOf(managerSession.getPermission().getId()));

        Manager updatedManager = managerService.saveManager(managerForm);
        session.setAttribute("manager", updatedManager);

        return "redirect:/profile/detail";
    }
}
