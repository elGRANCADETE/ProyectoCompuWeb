package com.eventoslive.eventosliveapp.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
    name = "event_user",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "event_id")
    )


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
