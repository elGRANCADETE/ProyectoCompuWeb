package com.eventoslive.eventosliveapp.controller;

import com.eventoslive.eventosliveapp.model.Event;
import com.eventoslive.eventosliveapp.service.EventService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody Event event) {
        Event updatedEvent = eventService.updateEvent(eventId, event);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Event> createEvent(@ModelAttribute Event event, 
                                             @RequestParam("eventCover") MultipartFile eventCover, 
                                             Principal principal) {

        String username = principal.getName(); // Obtiene el nombre de usuario del usuario autenticado
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
        model.addAttribute("events", events);
        return "events"; // Nombre del archivo HTML en la carpeta templates
    }

    // Other endpoints as needed...
}

