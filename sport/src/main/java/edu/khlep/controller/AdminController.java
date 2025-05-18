package edu.khlep.controller;

import java.util.List;

import edu.khlep.model.AppUser;
import edu.khlep.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService service;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        List<AppUser> users = service.findAll();
        model.addAttribute("users", users); 
        return "admin/dashboard";
    }
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        AppUser user = service.findById(id);
        model.addAttribute("user", user);
        return "admin/edit_user";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") AppUser cuser) {
        AppUser existing = service.findById(cuser.getId());
        String raw = cuser.getPassword();
        if (raw != null && !raw.isBlank()) {
            existing.setPassword(passwordEncoder.encode(raw));
            service.updateUser(existing);
        }
        else{
            service.updateUser(cuser);
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return "redirect:/admin/dashboard";
    }
}
