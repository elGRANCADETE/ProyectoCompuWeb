package com.eventoslive.eventosliveapp.repository;

import com.eventoslive.eventosliveapp.model.EventRecommendation;
import com.eventoslive.eventosliveapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRecommendationRepository extends JpaRepository<EventRecommendation, Long> {
    List<EventRecommendation> findByRecipient(User user);
}

