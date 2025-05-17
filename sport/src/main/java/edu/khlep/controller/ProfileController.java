package edu.khlep.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.khlep.model.AppUser;
import edu.khlep.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public ProfileController(UserService userService,
                             PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping
    public String viewProfile(Model model) {
        AppUser cuser = userService.getCurrentUser();
        if (cuser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", cuser);
        return "profile";   
    }
    @GetMapping("/update")
    public String showEditProfile(Model model) {
        AppUser cuser = userService.getCurrentUser();
        model.addAttribute("user", cuser);
        return "edit_profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") AppUser formUser) {
        AppUser existing = userService.findById(formUser.getId());
        existing.setUsername(formUser.getUsername());
        if (formUser.getPassword() != null && !formUser.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(formUser.getPassword()));
        }
        userService.updateUser(existing);
        return "redirect:/profile?success";
    }
    
}
