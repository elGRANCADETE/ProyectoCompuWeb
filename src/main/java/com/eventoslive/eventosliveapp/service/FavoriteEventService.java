package com.eventoslive.eventosliveapp.service;

import com.eventoslive.eventosliveapp.model.FavoriteEvent;
import com.eventoslive.eventosliveapp.repository.FavoriteEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteEventService {
    private final FavoriteEventRepository favoriteEventRepository;

    public FavoriteEventService(FavoriteEventRepository favoriteEventRepository) {
        this.favoriteEventRepository = favoriteEventRepository;
    }

    @Transactional
    public FavoriteEvent addFavoriteEvent(FavoriteEvent favoriteEvent) {
        if (favoriteEvent == null) {
            throw new IllegalArgumentException("FavoriteEvent no puede ser nulo");
        }
        return favoriteEventRepository.save(favoriteEvent);
    }

    // Otros métodos necesarios...
}
