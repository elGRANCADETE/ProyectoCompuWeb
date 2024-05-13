package com.eventoslive.eventosliveapp.controller;


import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoslive.eventosliveapp.model.Event;
import com.eventoslive.eventosliveapp.model.User;
import com.eventoslive.eventosliveapp.service.EventRecommendationService;
import com.eventoslive.eventosliveapp.service.EventService;
import com.eventoslive.eventosliveapp.service.UserService;




@Controller
public class WebController {

 
    private final UserService userService;
    private final EventService eventService;
    private final EventRecommendationService eventRecommendationService;

    public WebController(UserService userService, EventService eventService, EventRecommendationService eventRecommendationService) {
        this.userService = userService;
        this.eventService = eventService;
        this.eventRecommendationService = eventRecommendationService;
    }


    @GetMapping("/")
    public String redirectToForum() {
        return "redirect:/forum";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", defaultValue = "false") boolean loginError, Model model) {
        if (loginError) {
            model.addAttribute("loginError", "Your username or password is invalid.");
        }
        return "login";
    }

    @GetMapping("/users/register")
    public String register() {
        return "register";
    }

    @GetMapping("/users/registerConfirm")
    public String registerConfirm() {
        return "registerConfirm";
    }

    @GetMapping("/user/home")
    public String userHome(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "userHomePage";
    }

    @GetMapping("/organizer/home")
    public String organizerHome(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "organizerHomePage";
    }

    @GetMapping("/admin/home")
    public String adminHome(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "adminHomePage";
    }

    @GetMapping("/organizer/events/create")
    public String organizerNewEvent(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "newEvent";
    }

    @GetMapping("/organizer/events/my-events")
    public String myEvents(Model model, Principal principal) {
        String username = principal.getName();
        List<Event> events = eventService.getEventsByOrganizer(username);
        model.addAttribute("events", events);
        return "myEvents";
    }


    @GetMapping("/admin/manage-users")
    public String manageUsers(Model model) {
    List<User> users = userService.getAllUsers();
    model.addAttribute("users", users);
    return "manageUsers";
}

// En WebController

@GetMapping("/admin/events/manage")
public String manageEvents(Model model) {
    List<Event> events = eventService.getAllEvents();
    model.addAttribute("events", events);

    Long totalNumberOfEvents = eventService.getTotalNumberOfEvents();
    model.addAttribute("totalNumberOfEvents", totalNumberOfEvents);

    return "eventManagement";
}


    @GetMapping("/admin/events/edit/{eventId}")
    public String editEvent(@PathVariable Long eventId, Model model) {
        Event event = eventService.getEventById(eventId);
        model.addAttribute("event", event);
        return "editEvent";
    }

    @PostMapping("/admin/events/delete/{eventId}")
    public String deleteEvent(@PathVariable Long eventId, RedirectAttributes redirectAttributes) {
        try {
            eventService.deleteEvent(eventId);
            redirectAttributes.addFlashAttribute("successMessage", "Evento eliminado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el evento.");
        }
        return "redirect:/admin/events/manage";
    }

   @PostMapping("/admin/events/update")
public String updateEvent(@ModelAttribute Event updatedEvent, 
                          @RequestParam("eventCover") MultipartFile eventCover, 
                          RedirectAttributes redirectAttributes) {
    try {
        eventService.updateEvent(updatedEvent.getId(), updatedEvent, eventCover);
        redirectAttributes.addFlashAttribute("successMessage", "Evento actualizado con éxito.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar el evento.");
    }
    return "redirect:/admin/events/manage";
}

@GetMapping("/admin/users/edit/{id}")
public String editUser(@PathVariable Long id, Model model) {
    Optional<User> userOptional = userService.findById(id);  // Esto devuelve un Optional
    if (!userOptional.isPresent()) {
        // Usuario no encontrado, manejar apropiadamente
        // Podrías redirigir al usuario a una página de error o a la lista de usuarios
        return "redirect:/admin/manage-users";
    }
    User user = userOptional.get();  // Aquí obtienes el User si está presente
    model.addAttribute("user", user);
    return "editUser";
}

@PostMapping("/admin/users/update")
public String updateUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
    try {
        userService.updateUser(user);  // Asegúrate de que este método exista y esté correctamente implementado en UserService
        redirectAttributes.addFlashAttribute("successMessage", "Usuario actualizado con éxito.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar el usuario: " + e.getMessage());
    }
    return "redirect:/admin/manage-users";  // Asegúrate de que esta ruta sea correcta
}

@GetMapping("/users/profile/{id}")
public String showUserProfile(@PathVariable Long id, Model model) {
    Optional<User> userOptional = userService.findById(id);
    if (userOptional.isPresent()) {
        model.addAttribute("user", userOptional.get());
        return "userProfile";
    } else {
        return "redirect:/admin/manage-users";  // Redirige si el usuario no se encuentra
    }
}

@GetMapping("/forum")
public String forum(Model model) {
    List<Event> events = eventService.getAllEventsOrdered();
    model.addAttribute("events", events);
    return "forum";
}

@GetMapping("/user/home/savedEvents")
public String showSavedEvents(Model model, Principal principal) {
    String username = principal.getName();
    List<Event> savedEvents = eventService.findSavedEventsByUsername(username);
    model.addAttribute("savedEvents", savedEvents);
    return "savedEvents";  // Asegúrate de que esta vista existe
}

@PostMapping("/user/events/remove/{eventId}")
public String removeSavedEvent(@PathVariable Long eventId, Principal principal, RedirectAttributes redirectAttributes) {
    try {
        eventService.removeEventFromUser(eventId, principal.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Event removed successfully.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error removing event: " + e.getMessage());
    }
    return "redirect:/user/home/savedEvents"; // Or wherever you want to redirect
}

@GetMapping("/user/home/userForum")
public String userForum(Model model, Principal principal) {
    User user = userService.findByUsername(principal.getName());
    model.addAttribute("user", user);
    List<Event> events = eventService.getAllEvents();
    System.out.println("Número de eventos: " + events.size()); // Log para depuración
    model.addAttribute("events", events);
    return "userForum";  
}
@GetMapping("/user/home/userForum/shareEvents")
public String showShareEventPage(@RequestParam("eventId") Long eventId, Model model, Principal principal) {
    // Asegurarse de que el usuario está autenticado y obtener sus datos
    User user = userService.findByUsername(principal.getName());
    model.addAttribute("user", user);

    // Obtener el evento específico por ID para compartir
    Event event = eventService.getEventById(eventId);
    if (event == null) {
        return "redirect:/user/home/userForum";  // Redirigir si el evento no existe
    }
    model.addAttribute("event", event);

    // Opcional: Listar todos los usuarios para seleccionar a quién compartir (ajustar según necesidad)
    List<User> allUsers = userService.getAllUsers();
    model.addAttribute("users", allUsers);

    return "shareEvent";  // Nombre de la vista Thymeleaf para compartir eventos
}

@GetMapping("/user/home/recommendEvents")
public String viewRecommendedEvents(Model model, Principal principal) {
    User user = userService.findByUsername(principal.getName());
    model.addAttribute("recommendations", eventRecommendationService.getRecommendationsForUser(user.getId()));
    return "recommendEvents";
}


} 
