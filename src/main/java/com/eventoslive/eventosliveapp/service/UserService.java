package com.eventoslive.eventosliveapp.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.eventoslive.eventosliveapp.exceptions.CustomException;
import com.eventoslive.eventosliveapp.model.User;
import com.eventoslive.eventosliveapp.repository.UserRepository;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(User user) {
        // Verificar si el username ya está en uso
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new CustomException("Username ya está en uso");
        }

        // Verificar si el email ya está en uso
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new CustomException("Email ya está en uso");
        }

        // Establecer el rol por defecto y encriptar la contraseña
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Generar y asignar el token de verificación
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setVerified(false); // Inicialmente, el usuario no está verificado

        sendVerificationEmail(user, token); // Envía el correo electrónico

        return userRepository.save(user); // Guardar el usuario
    }

    @Transactional
    public User changeUserRole(Long userId, String newRole) {
        if (userId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")); // Cambia RuntimeException por tu excepción
                                                                            // personalizada
        user.setRole(newRole);
        return userRepository.save(user);
    }

    @Transactional
    public void verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token)
            .orElseThrow(() -> new CustomException("Token de verificación inválido"));

        if (user.isVerified()) {
            throw new CustomException("El usuario ya ha sido verificado");
        }

        user.setVerified(true);
        userRepository.save(user);
    }

    private void sendVerificationEmail(User user, String token) {
        String apiKey = "TU_API_KEY_SENDGRID";
        Email from = new Email("tucorreo@example.com");
        String subject = "Verifica tu correo electrónico";
        Email to = new Email(user.getEmail());
        String verificationUrl = "http://tu-dominio.com/verify?token=" + token;
        Content content = new Content("text/plain",
                "Haz clic en el siguiente enlace para verificar tu correo electrónico: " + verificationUrl);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            // Maneja la excepción
        }
    }

    // Otros métodos del servicio...
}
