package com.eventoslive.eventosliveapp.repository;

import com.eventoslive.eventosliveapp.model.Event;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    // Aquí puedes agregar métodos para buscar eventos específicos si es necesario

    List<Event> findByUsernameOrderByStartTimeDesc(String username);
    
    List<Event> findAllByOrderByIdDesc();
}
