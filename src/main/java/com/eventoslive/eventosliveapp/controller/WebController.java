package com.eventoslive.eventosliveapp.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventoslive.eventosliveapp.model.Event;
import com.eventoslive.eventosliveapp.model.User;
import com.eventoslive.eventosliveapp.service.EventService;
import com.eventoslive.eventosliveapp.service.UserService;

@Controller
public class WebController {

    @Autowired
    private UserService userService; // Inyección de la instancia de UserService

    @Autowired
    private EventService eventService; // Inyección de la instancia de EventService

    @GetMapping("/")
    public String redirectToForum() {
        return "redirect:/forum";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", defaultValue = "false") boolean loginError, Model model) {
        if (loginError) {
            model.addAttribute("loginError", "Your username or password is invalid.");
        }
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

    @GetMapping("/organizer/home")
    public String organizerHome(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "organizerHomePage";
    }

    @GetMapping("/admin/home")
    public String adminHome(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "adminHomePage";
    }

    @GetMapping("/organizer/events/create")
    public String organizerNewEvent(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "newEvent";
    }

    @GetMapping("/organizer/events/my-events")
    public String myEvents(Model model, Principal principal) {
        String username = principal.getName();
        List<Event> events = eventService.getEventsByOrganizer(username);
        model.addAttribute("events", events);
        return "myEvents";
    }

    @GetMapping("/forum")
    public String forum(Model model) {
        List<Event> events = eventService.getAllEventsOrdered();
        model.addAttribute("events", events);
        return "forum";
    }

    // Otros mapeos...
}
