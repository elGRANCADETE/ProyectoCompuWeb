package com.eventoslive.eventosliveapp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.eventoslive.eventosliveapp.exceptions.CustomException;
import com.eventoslive.eventosliveapp.model.Event;
import com.eventoslive.eventosliveapp.model.User;
import com.eventoslive.eventosliveapp.repository.EventRepository;
import com.eventoslive.eventosliveapp.repository.UserRepository;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final Path rootLocation;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.rootLocation = Paths.get("src/main/resources/static/event_gallery").toAbsolutePath().normalize();
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new CustomException("No se pudo inicializar la carpeta para las imágenes: " + e.getMessage());
        }
    }

    @Transactional
    public Event createEvent(Event event, String username, MultipartFile eventCover) {
        if (eventCover != null && !eventCover.isEmpty()) {
            try {
                String filename = resizeAndStoreImage(eventCover);
                event.setEventCoverPath(filename);
            } catch (IOException e) {
                throw new CustomException("Error al procesar la imagen: " + e.getMessage());
            }
        }
        event.setUsername(username);
        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(long eventId, Event updatedEvent, MultipartFile eventCover) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new CustomException("Evento no encontrado con ID: " + eventId));

        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setStartTime(updatedEvent.getStartTime());
        event.setDurationMinutes(updatedEvent.getDurationMinutes());

        if (eventCover != null && !eventCover.isEmpty()) {
            try {
                String filename = resizeAndStoreImage(eventCover);
                event.setEventCoverPath(filename);
            } catch (IOException e) {
                throw new CustomException("Error al procesar la imagen: " + e.getMessage());
            }
        }

        return eventRepository.save(event);
    }

    private String resizeAndStoreImage(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID().toString() + ".jpg";
        Path destinationFile = this.rootLocation.resolve(filename).normalize();

        Thumbnails.of(file.getInputStream())
            .size(500, 500)
            .outputFormat("jpg")
            .toFile(destinationFile.toFile());

        return filename;
    }

    @Transactional
    public void deleteEvent(long eventId) {
        eventRepository.deleteById(eventId);
    }

    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Event> getAllEventsOrdered() {
        return eventRepository.findAllByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public Event getEventById(long eventId) {
        return eventRepository.findById(eventId)
            .orElseThrow(() -> new CustomException("Evento no encontrado con ID: " + eventId));
    }

    @Transactional(readOnly = true)
public Long getTotalNumberOfEvents() {
    return eventRepository.count(); // Utiliza el método count() proporcionado por JpaRepository
}

@Transactional(readOnly = true)
public List<Event> findSavedEventsByUsername(String username) {
    return eventRepository.findSavedByUsername(username); // Asume que este método está definido en EventRepository
}

@Transactional(readOnly = true)
public List<Event> getEventsByOrganizer(String username) {
    return eventRepository.findByUsernameOrderByStartTimeDesc(username); // Asume que este método está definido en EventRepository
}

@Transactional
public void removeEventFromUser(Long eventId, String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado con username: " + username));
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + eventId));

    // Verificar si el evento está realmente asociado con el usuario
    if (user.getSavedEvents().contains(event)) {
        user.getSavedEvents().remove(event); // Eliminar el evento de la lista de eventos guardados del usuario
        userRepository.save(user); // Guardar el usuario con la lista actualizada
    } else {
        throw new RuntimeException("El evento con ID: " + eventId + " no está guardado por el usuario con username: " + username);
    }
}


}
