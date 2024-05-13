package com.eventoslive.eventosliveapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventoslive.eventosliveapp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // Cambio para devolver Optional
    Optional<User> findByEmail(String email); // Cambio opcional para consistencia
    Optional<User> findByVerificationToken(String token);
}
