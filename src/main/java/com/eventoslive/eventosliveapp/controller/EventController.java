package com.eventoslive.eventosliveapp.controller;
import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eventoslive.eventosliveapp.model.Event;
import com.eventoslive.eventosliveapp.model.EventRecommendation;
import com.eventoslive.eventosliveapp.service.EventRecommendationService;
import com.eventoslive.eventosliveapp.service.EventService;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final EventRecommendationService eventRecommendationService;
    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    public EventController(EventService eventService, EventRecommendationService eventRecommendationService) {
        this.eventService = eventService;
        this.eventRecommendationService = eventRecommendationService;
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @ModelAttribute Event event, @RequestParam("eventCover") MultipartFile eventCover) {
        Event updatedEvent = eventService.updateEvent(eventId, event, eventCover);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Event> createEvent(@ModelAttribute Event event, @RequestParam("eventCover") MultipartFile eventCover, Principal principal) {
        String username = principal.getName();
        Event newEvent = eventService.createEvent(event, username, eventCover);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEvent);
    }

    @GetMapping("/details/{eventId}")
    public String showEventDetails(@PathVariable Long eventId, Model model) {
        Event event = eventService.getEventById(eventId);
        model.addAttribute("event", event);
        return "event-details";
    }

    @GetMapping("/list")
    public String showAllEvents(Model model) {
        List<Event> events = eventService.getAllEvents();
        Long totalNumberOfEvents = eventService.getTotalNumberOfEvents();
        model.addAttribute("events", events);
        model.addAttribute("totalNumberOfEvents", totalNumberOfEvents);
        return "events";
    }

    @GetMapping("/user/home/savedEvents")
    public String showSavedEvents(Model model, Principal principal) {
        String username = principal.getName();
        List<Event> savedEvents = eventService.findSavedEventsByUsername(username);
        model.addAttribute("savedEvents", savedEvents);
        return "/savedEvents";
    }

    @PostMapping("/save/{eventId}")
    public ResponseEntity<?> saveEvent(@PathVariable Long eventId, Principal principal) {
        try {
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el evento: " + e.getMessage());
        }
    }

    @PostMapping("/recommend")
    public ResponseEntity<EventRecommendation> recommendEvent(@RequestParam("eventId") Long eventId, @RequestParam("senderId") Long senderId, @RequestParam("recipientId") Long recipientId) {
        if (eventId == null || senderId == null || recipientId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        EventRecommendation newRecommendation = eventRecommendationService.recommendEvent(eventId, senderId, recipientId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRecommendation);
    }

    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<List<EventRecommendation>> getRecommendations(@PathVariable Long userId) {
        List<EventRecommendation> recommendations = eventRecommendationService.getRecommendationsForUser(userId);
        return ResponseEntity.ok(recommendations);
    }

    @PostMapping("/recommendations/create")
    public ResponseEntity<EventRecommendation> createRecommendation(@RequestBody EventRecommendation recommendation) {
        EventRecommendation newRecommendation = eventRecommendationService.createRecommendation(recommendation);
        return ResponseEntity.ok(newRecommendation);
    }
}
