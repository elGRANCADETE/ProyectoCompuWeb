package com.eventoslive.eventosliveapp.controller;

import com.eventoslive.eventosliveapp.exceptions.CustomException;
import com.eventoslive.eventosliveapp.model.User;
import com.eventoslive.eventosliveapp.service.UserService;

import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("confirmPassword", "Passwords do not match"));
        }

        try {
            User newUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            // Para cualquier otra excepción, podrías querer manejarla de manera diferente
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Internal server error"));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String token) {
        try {
            userService.verifyUser(token);
            return ResponseEntity.ok("Cuenta verificada con éxito");
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Other endpoints as needed...
}



