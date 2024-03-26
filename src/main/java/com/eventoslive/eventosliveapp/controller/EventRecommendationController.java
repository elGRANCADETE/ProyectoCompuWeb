package com.eventoslive.eventosliveapp.controller;

import com.eventoslive.eventosliveapp.model.EventRecommendation;
import com.eventoslive.eventosliveapp.service.EventRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class EventRecommendationController {
    
    private final EventRecommendationService eventRecommendationService;

    @Autowired
    public EventRecommendationController(EventRecommendationService eventRecommendationService) {
        this.eventRecommendationService = eventRecommendationService;
    }

    @PostMapping
    public ResponseEntity<EventRecommendation> createRecommendation(@RequestBody EventRecommendation recommendation) {
        EventRecommendation newRecommendation = eventRecommendationService.recommendEvent(recommendation);
        return ResponseEntity.ok(newRecommendation);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<EventRecommendation>> getRecommendations(@PathVariable Long userId) {
        List<EventRecommendation> recommendations = eventRecommendationService.getRecommendationsForUser(userId);
        return ResponseEntity.ok(recommendations);
    }

    // Other endpoints as needed...
}
