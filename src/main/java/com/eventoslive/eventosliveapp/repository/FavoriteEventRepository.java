package com.eventoslive.eventosliveapp.repository;

import com.eventoslive.eventosliveapp.model.FavoriteEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteEventRepository extends JpaRepository<FavoriteEvent, Long> {
    // Métodos para encontrar eventos favoritos de un usuario, etc.
}

