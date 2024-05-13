package com.eventoslive.eventosliveapp.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventoslive.eventosliveapp.exceptions.CustomException;
import com.eventoslive.eventosliveapp.model.User;
import com.eventoslive.eventosliveapp.repository.UserRepository;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Crear un nuevo usuario con validación de disponibilidad de username y email.
    @Transactional
    public User createUser(User user) {
        userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new CustomException("El username ya está en uso");
        });
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new CustomException("El email ya está en uso");
        });

        // Encriptación de la contraseña y configuración inicial del usuario
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerified(false);
        user.setActive(true);

        sendVerificationEmail(user, user.getVerificationToken());
        return userRepository.save(user);
    }

    // Cambia el rol de un usuario existente
    @Transactional
    public User changeUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new CustomException("Usuario no encontrado con ID: " + userId));
        user.setRole(newRole);
        return userRepository.save(user);
    }

    // Verificar usuario con un token
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

    // Enviar correo de verificación
    private void sendVerificationEmail(User user, String token) {
        String apiKey = "TU_API_KEY_SENDGRID";
        Email from = new Email("tucorreo@example.com");
        String subject = "Verifica tu correo electrónico";
        Email to = new Email(user.getEmail());
        Content content = new Content("text/plain", "Haz clic en el siguiente enlace para verificar tu correo: http://tu-dominio.com/verify?token=" + token);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            throw new CustomException("Error al enviar el correo de verificación");
        }
    }

    // Encuentra un usuario por username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                             .orElseThrow(() -> new CustomException("Usuario no encontrado con username: " + username));
    }

    // Obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Buscar un usuario por ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Actualizar un usuario existente
    @Transactional
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                                          .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + user.getId()));
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());
        existingUser.setActive(user.isActive());
        userRepository.save(existingUser);
        return existingUser;
    }

     // Método para verificar si el nombre de usuario está disponible
     public boolean isUsernameAvailable(String username) {
        return !userRepository.findByUsername(username).isPresent();
    }

    // Método para verificar si el correo electrónico está disponible
    public boolean isEmailAvailable(String email) {
        return !userRepository.findByEmail(email).isPresent();
    }

}
