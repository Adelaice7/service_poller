package com.rmeunier.servicepoller.controller;

import com.rmeunier.servicepoller.model.User;
import com.rmeunier.servicepoller.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registrationPage";
    }

    @PostMapping("/process_register")
    public String processRegistration(User user, Model model) {
        userService.addUser(user);

        model.addAttribute("registerSuccess", true);
        return "loginPage";
    }

}
