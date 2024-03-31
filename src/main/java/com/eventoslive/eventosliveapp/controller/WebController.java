package com.eventoslive.eventosliveapp.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.eventoslive.eventosliveapp.model.User;
import com.eventoslive.eventosliveapp.service.UserService;

@Controller
public class WebController {

    @Autowired
    private UserService userService; // Inyección de la instancia de UserService

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/users/register")
    public String register() {
        return "register";
    }

    @GetMapping("/users/registerConfirm")
    public String registerConfirm() {
        return "registerConfirm";
    }

    @GetMapping("/user/home")
    public String userHome(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "userHomePage";
    }

    // Otros mapeos...
}



