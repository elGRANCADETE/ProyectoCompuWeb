package com.eventoslive.eventosliveapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/users/register")
    public String register() {
        return "register";
    }

    // Otros mapeos...
}


