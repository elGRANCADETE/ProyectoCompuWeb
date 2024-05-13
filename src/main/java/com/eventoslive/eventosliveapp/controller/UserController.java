package com.eventoslive.eventosliveapp.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoslive.eventosliveapp.exceptions.CustomException;
import com.eventoslive.eventosliveapp.model.User;
import com.eventoslive.eventosliveapp.service.EventService;
import com.eventoslive.eventosliveapp.service.UserBlockService;
import com.eventoslive.eventosliveapp.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserBlockService userBlockService;
    private final EventService eventService;

    public UserController(UserService userService, EventService eventService, UserBlockService userBlockService) {
        this.userService = userService;
        this.eventService = eventService;
        this.userBlockService = userBlockService;
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
            return ResponseEntity.badRequest().body(Collections.singletonMap("confirmPassword", "Passwords do not match"));
        }

        try {
            User newUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Internal server error"));
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

    @PostMapping("/check-availability")
    public ResponseEntity<?> checkAvailability(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");

        boolean isUsernameAvailable = userService.isUsernameAvailable(username);
        boolean isEmailAvailable = userService.isEmailAvailable(email);

        Map<String, Object> response = new HashMap<>();
        response.put("available", isUsernameAvailable && isEmailAvailable);
        response.put("usernameUnavailable", !isUsernameAvailable);
        response.put("emailUnavailable", !isEmailAvailable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/users/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/admin/users/update")
    public String updateUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Usuario actualizado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar el usuario: " + e.getMessage());
        }
        return "redirect:/admin/manage-users";
    }

    @GetMapping("/users/profile/{id}")
    public String showUserProfile(@PathVariable Long id, Model model) {
        Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            return "redirect:/admin/manage-users"; // O alguna página de error
        }
        model.addAttribute("user", userOptional.get());
        return "userProfile";
    }

    @ModelAttribute("users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/admin/manageUsers")
    public String manageUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "manageUsers";  // Carga 'manageUsers.html'
    }

    // Other endpoints as needed...
}
