package com.eventoslive.eventosliveapp.service;

import com.eventoslive.eventosliveapp.model.Event;
import com.eventoslive.eventosliveapp.repository.EventRepository;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final Path rootLocation;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.rootLocation = Paths.get("src/main/resources/static/event_gallery").toAbsolutePath().normalize(); // Asignación
                                                                                                               // en el
                                                                                                               // constructor
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar la carpeta para las imágenes", e);
        }
    }

    @Transactional
    public Event createEvent(Event event, String username, MultipartFile eventCover) {
        if (eventCover != null && !eventCover.isEmpty()) {
            try {
                String filename = resizeAndStoreImage(eventCover); // Redimensiona y almacena la imagen
                event.setEventCoverPath(filename); // Guarda la ruta de la imagen
            } catch (IOException e) {
                throw new RuntimeException("Error al procesar la imagen", e);
            }
        }
        event.setUsername(username); // Establecer el nombre de usuario
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
    public List<Event> getAllEventsOrdered() {
        return eventRepository.findAllByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public Event getEventById(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + eventId));
    }

    // redimensiona las imagenes que sube el coordinador antes de guardarlas en la
    // base de datos
    private String resizeAndStoreImage(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID().toString() + ".jpg"; // Genera un nombre de archivo único
        Path destinationFile = this.rootLocation.resolve(filename).normalize();

        // Redimensionar la imagen a un ancho de 500px manteniendo la proporción de
        // aspecto
        Thumbnails.of(file.getInputStream())
                .size(500, 500)
                .outputFormat("jpg")
                .toFile(destinationFile.toFile());

        return filename;
    }

    public List<Event> getEventsByOrganizer(String username) {
        return eventRepository.findByUsernameOrderByStartTimeDesc(username);
    }

    // Otros métodos según sea necesario...
}
