package com.eventoslive.eventosliveapp.service;

import com.eventoslive.eventosliveapp.model.User;
import com.eventoslive.eventosliveapp.model.UserBlock;
import com.eventoslive.eventosliveapp.repository.UserBlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserBlockService {
    private final UserBlockRepository userBlockRepository;

    @Autowired
    public UserBlockService(UserBlockRepository userBlockRepository) {
        this.userBlockRepository = userBlockRepository;
    }

    @Transactional
    public UserBlock blockUser(UserBlock userBlock) {
        // Añade lógica adicional si es necesario
        return userBlockRepository.save(userBlock);
    }

    @Transactional(readOnly = true)
    public List<UserBlock> getBlocksForUser(User user) {
        return userBlockRepository.findByBlocker(user);
    }

    // Otros métodos según sea necesario...
}

