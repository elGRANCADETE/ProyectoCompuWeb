package com.eventoslive.eventosliveapp.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role; // Roles (ADMIN, ORGANIZER, USER)

    @Pattern(regexp = "^[0-9]{9}$", message = "Phone number must be 9 digits")
    private String phoneNumber;

    @Email(message = "Email should be valid")
    private String email;

    @Transient
    private String confirmPassword; 

    private String verificationToken;

    @Column(nullable = false)
    private boolean isVerified = false;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToMany(mappedBy = "users")
    private Set<Event> savedEvents = new HashSet<>();

    public void saveEvent(Event event) {
        savedEvents.add(event);
        event.getUsers().add(this);
    }

    public void removeSavedEvent(Event event) {
        savedEvents.remove(event);
        event.getUsers().remove(this);
    }
}
