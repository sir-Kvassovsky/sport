package edu.khlep.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import edu.khlep.model.AppUser;
import edu.khlep.service.UserService;

@Controller
public class AuthController {
    private final UserService userService;
 
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/sign-up")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new AppUser());
        return "sign-up";
    }
    
    @PostMapping("/sign-up")
    public String SignUpUser(@ModelAttribute("user") AppUser user) {
        try {
            userService.SignUpNewUser(user);
        } catch (Exception e) {
            return "sign-up";
        }
        return "redirect:/login?sign-uped";
    }
    @GetMapping("/login")
    public String login() {
        return "login"; 
    }
}
