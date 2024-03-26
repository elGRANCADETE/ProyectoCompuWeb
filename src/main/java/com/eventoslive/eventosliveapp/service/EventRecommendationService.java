package com.eventoslive.eventosliveapp.service;

import com.eventoslive.eventosliveapp.model.EventRecommendation;
import com.eventoslive.eventosliveapp.repository.EventRecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EventRecommendationService {
    private final EventRecommendationRepository eventRecommendationRepository;

    @Autowired
    public EventRecommendationService(EventRecommendationRepository eventRecommendationRepository) {
        this.eventRecommendationRepository = eventRecommendationRepository;
    }

    @Transactional
    public EventRecommendation recommendEvent(EventRecommendation eventRecommendation) {
        // Añade lógica adicional si es necesario
        return eventRecommendationRepository.save(eventRecommendation);
    }

    @Transactional(readOnly = true)
    public List<EventRecommendation> getRecommendationsForUser(Long userId) {
        // Implementa la lógica para recuperar recomendaciones
        // Puede ser necesario ampliar el repositorio para este método
    }

    // Otros métodos según sea necesario...
}
