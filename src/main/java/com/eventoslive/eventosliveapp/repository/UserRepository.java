package com.eventoslive.eventosliveapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventoslive.eventosliveapp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
