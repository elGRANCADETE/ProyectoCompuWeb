package com.eventoslive.eventosliveapp.service;

import com.eventoslive.eventosliveapp.model.Event;
import com.eventoslive.eventosliveapp.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Event createEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("El evento no puede ser nulo.");
        }
        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(long eventId, Event updatedEvent) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + eventId));
        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setStartTime(updatedEvent.getStartTime());
        event.setDurationMinutes(updatedEvent.getDurationMinutes());
        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new RuntimeException("Evento no encontrado con ID: " + eventId);
        }
        eventRepository.deleteById(eventId);
    }

    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Event getEventById(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + eventId));
    }

    // Otros métodos según sea necesario...
}

