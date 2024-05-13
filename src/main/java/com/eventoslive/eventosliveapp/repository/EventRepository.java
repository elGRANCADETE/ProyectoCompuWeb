package com.eventoslive.eventosliveapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eventoslive.eventosliveapp.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    
    // Aquí puedes agregar métodos para buscar eventos específicos si es necesario
    List<Event> findByUsernameOrderByStartTimeDesc(String username);
    List<Event> findAllByOrderByIdDesc();

   
@Query("SELECT e FROM Event e JOIN e.users u WHERE u.username = :username")
List<Event> findSavedByUsername(@Param("username") String username);





    

}
