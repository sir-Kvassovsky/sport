package edu.khlep.controller;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.khlep.model.AppUser;
import edu.khlep.model.Event;
import edu.khlep.service.EventService;
import edu.khlep.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;
    private final EventService eventService;
    private final PasswordEncoder passwordEncoder;
    public ProfileController(UserService userService, 
                             PasswordEncoder passwordEncoder, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping
    public String viewProfile(Model model) {
        AppUser cuser = userService.getCurrentUser();
        if (cuser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", cuser);
        List<Event> subscribedEvents = eventService.getEventsForCurrentUser();
        model.addAttribute("subscribedEvents", subscribedEvents);
        return "user/profile";   
    }
    @GetMapping("/update")
    public String showEditProfile(Model model) {
        AppUser cuser = userService.getCurrentUser();
        model.addAttribute("user", cuser);
        return "user/edit_profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") AppUser formUser) {
        userService.updateUser(formUser);
        return "redirect:/profile?success";
    }
    @GetMapping("/update_password")
    public String showEditPassword(Model model) {
        AppUser cuser = userService.getCurrentUser();
        model.addAttribute("user", cuser);
        return "user/edit_password";
    }

    @PostMapping("/update_password")
    public String updatePassword(@ModelAttribute("user") AppUser formUser) {
        AppUser existing = userService.findById(formUser.getId());
        String raw = formUser.getPassword();
        if (raw != null && !raw.isBlank()) {
            existing.setPassword(passwordEncoder.encode(raw));
            userService.updateUser(existing);
        }
        return "redirect:/profile?success";
    }
    
}
