package com.eventoslive.eventosliveapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // Asegúrate de que tienes un método para '/login' si no utilizas el auto-configurado de Spring Security
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/users/register")
    public String register() {
        return "register"; // Este debería ser el nombre del archivo HTML sin la extensión
    }
}

