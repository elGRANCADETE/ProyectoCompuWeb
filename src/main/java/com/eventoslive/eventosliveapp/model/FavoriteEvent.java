package com.eventoslive.eventosliveapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite_events")
public class FavoriteEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    // Constructor, getters y setters...
}

