package com.eventoslive.eventosliveapp.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data   // Lombok annotation para generar getters, setters etc
@NoArgsConstructor
@Table(name = "event_recommendations")
public class EventRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private LocalDateTime timestamp;
}
