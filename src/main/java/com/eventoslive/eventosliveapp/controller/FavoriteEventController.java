package com.eventoslive.eventosliveapp.controller;

import com.eventoslive.eventosliveapp.model.FavoriteEvent;
import com.eventoslive.eventosliveapp.service.FavoriteEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
public class FavoriteEventController {
    private final FavoriteEventService favoriteEventService;
    
    public FavoriteEventController(FavoriteEventService favoriteEventService) {
        this.favoriteEventService = favoriteEventService;
    }

    @PostMapping
    public ResponseEntity<FavoriteEvent> addFavoriteEvent(@RequestBody FavoriteEvent favoriteEvent) {
        FavoriteEvent newFavoriteEvent = favoriteEventService.addFavoriteEvent(favoriteEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(newFavoriteEvent);
    }

    // Métodos para obtener y eliminar eventos favoritos...
}
