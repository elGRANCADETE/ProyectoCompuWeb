package com.eventoslive.eventosliveapp.service;

import com.eventoslive.eventosliveapp.model.User;
import com.eventoslive.eventosliveapp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        return userRepository.save(user);
    }

    @Transactional
    public User changeUserRole(Long userId, String newRole) {
        if (userId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo.");
        }
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found")); // Cambia RuntimeException por tu excepción personalizada
        user.setRole(newRole);
        return userRepository.save(user);
    }


    // Otros métodos del servicio...
}
