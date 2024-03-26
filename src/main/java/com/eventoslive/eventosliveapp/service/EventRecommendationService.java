package com.eventoslive.eventosliveapp.service;

import com.eventoslive.eventosliveapp.model.EventRecommendation;
import com.eventoslive.eventosliveapp.repository.EventRecommendationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventRecommendationService {
    private final EventRecommendationRepository eventRecommendationRepository;

    public EventRecommendationService(EventRecommendationRepository eventRecommendationRepository) {
        this.eventRecommendationRepository = eventRecommendationRepository;
    }

    @Transactional
    public EventRecommendation createRecommendation(EventRecommendation recommendation) {
        if (recommendation == null) {
            throw new IllegalArgumentException("La recomendación no puede ser nula");
        }
        return eventRecommendationRepository.save(recommendation);
    }


    @Transactional
    public EventRecommendation recommendEvent(EventRecommendation eventRecommendation) {
        if (eventRecommendation == null) {
            throw new IllegalArgumentException("La recomendación del evento no puede ser nula");
        }
        // Añade lógica adicional si es necesario
        return eventRecommendationRepository.save(eventRecommendation);
    }

    @Transactional(readOnly = true)
    public List<EventRecommendation> getRecommendationsForUser(Long userId) {
        // Implementa la lógica para recuperar recomendaciones
        // Puede ser necesario ampliar el repositorio para este método
        return new ArrayList<>(); // Retorno temporal
    }

    // Otros métodos según sea necesario...
}
