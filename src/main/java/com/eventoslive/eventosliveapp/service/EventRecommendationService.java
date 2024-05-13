package com.eventoslive.eventosliveapp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventoslive.eventosliveapp.model.Event;
import com.eventoslive.eventosliveapp.model.EventRecommendation;
import com.eventoslive.eventosliveapp.model.User;
import com.eventoslive.eventosliveapp.repository.EventRecommendationRepository;
import com.eventoslive.eventosliveapp.repository.EventRepository;
import com.eventoslive.eventosliveapp.repository.UserRepository;

@Service
public class EventRecommendationService {
    private final EventRecommendationRepository eventRecommendationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public EventRecommendationService(EventRecommendationRepository eventRecommendationRepository,
                                      UserRepository userRepository,
                                      EventRepository eventRepository) {
        this.eventRecommendationRepository = eventRecommendationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public EventRecommendation createRecommendation(EventRecommendation recommendation) {
        if (recommendation == null) {
            throw new IllegalArgumentException("La recomendación no puede ser nula");
        }
        return eventRecommendationRepository.save(recommendation);
    }

    @Transactional
    public EventRecommendation recommendEvent(Long eventId, Long senderId, Long recipientId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));
        User sender = userRepository.findById(senderId).orElseThrow(() -> new IllegalArgumentException("Invalid sender ID"));
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new IllegalArgumentException("Invalid recipient ID"));

        EventRecommendation recommendation = new EventRecommendation();
        recommendation.setEvent(event);
        recommendation.setSender(sender);
        recommendation.setRecipient(recipient);

        return eventRecommendationRepository.save(recommendation);
    }

    @Transactional(readOnly = true)
    public List<EventRecommendation> getRecommendationsForUser(Long userId) {
        User recipient = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return eventRecommendationRepository.findByRecipient(recipient);
    }
}
