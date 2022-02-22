package com.rmeunier.servicepoller.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class MainController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

    @RequestMapping("/login?success")
    public String loginSuccess(Model model, Principal principal) {
        model.addAttribute("principal", principal);
        return "index";
    }

    @RequestMapping("/login?error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "loginPage";
    }

    @RequestMapping("/services")
    public String services() {
        return "index";
    }
}
