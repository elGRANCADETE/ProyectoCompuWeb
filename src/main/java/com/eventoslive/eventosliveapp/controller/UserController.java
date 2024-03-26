package com.eventoslive.eventosliveapp.controller;

import com.eventoslive.eventosliveapp.model.User;
import com.eventoslive.eventosliveapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{userId}/changeRole")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> changeUserRole(@PathVariable Long userId, @RequestBody String newRole) {
        User updatedUser = userService.changeUserRole(userId, newRole);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        user.setRole("USER"); // Establece el rol por defecto a USER
        User newUser = userService.createUser(user);
        return ResponseEntity.ok(newUser);
    }

    // Other endpoints as needed...
}


