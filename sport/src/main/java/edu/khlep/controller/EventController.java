package edu.khlep.controller;

import edu.khlep.model.AppUser;
import edu.khlep.service.EventService;
import edu.khlep.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @PostMapping("/subscribe/{id}")
    @Secured("ROLE_PARTICIPANT")
    public String subscribeToEvent(@PathVariable Long id) {
        AppUser currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            eventService.registerCurrentUserForEvent(id);
        }
        return "redirect:/main";
    }

    @PostMapping("/unsubscribe/{id}")
    @Secured("ROLE_PARTICIPANT")
    public String unsubscribeFromEvent(@PathVariable Long id) {
        AppUser currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            eventService.unregisterCurrentUserFromEvent(id);
        }
        return "redirect:/main";
    }
    
}
