package com.eventoslive.eventosliveapp.repository;

import com.eventoslive.eventosliveapp.model.Event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    // Aquí puedes agregar métodos para buscar eventos específicos si es necesario
}
