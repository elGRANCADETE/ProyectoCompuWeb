package com.eventoslive.eventosliveapp.controller;

import com.eventoslive.eventosliveapp.model.EventRecommendation;
import com.eventoslive.eventosliveapp.service.EventRecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class EventRecommendationController {
    
    private final EventRecommendationService eventRecommendationService;
    
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

    @PostMapping("/recommend")
    public ResponseEntity<EventRecommendation> recommendEvent(@RequestBody EventRecommendation recommendation) {
        EventRecommendation newRecommendation = eventRecommendationService.createRecommendation(recommendation);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRecommendation);
    }

    // Other endpoints as needed...
}
