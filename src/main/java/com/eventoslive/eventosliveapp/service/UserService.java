package com.eventoslive.eventosliveapp.service;

import com.eventoslive.eventosliveapp.model.User;
import com.eventoslive.eventosliveapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(User user) {
        // Añade lógica para cifrar la contraseña y validar los datos aquí
        return userRepository.save(user);
    }

    // Otros métodos del servicio...
}
