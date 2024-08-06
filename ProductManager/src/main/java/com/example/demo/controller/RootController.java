package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RootController {
    @GetMapping("/")
    public String index() {
        return "redirect:/product/index";
    }
}
