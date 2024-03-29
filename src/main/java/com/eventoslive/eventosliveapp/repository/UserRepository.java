package com.eventoslive.eventosliveapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventoslive.eventosliveapp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    Optional<User> findByVerificationToken(String token);
}
