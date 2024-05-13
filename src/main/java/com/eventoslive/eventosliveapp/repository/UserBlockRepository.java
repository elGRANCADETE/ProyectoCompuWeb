package com.eventoslive.eventosliveapp.repository;

import com.eventoslive.eventosliveapp.model.User;
import com.eventoslive.eventosliveapp.model.UserBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {
    List<UserBlock> findByBlocker(User user);
    boolean existsByBlockerAndBlocked(User blocker, User blocked);
}

