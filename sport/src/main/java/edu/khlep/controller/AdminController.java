package edu.khlep.controller;

import java.util.List;
import java.util.stream.Collectors;

import edu.khlep.model.AppUser;
import edu.khlep.service.EventService;
import edu.khlep.service.UserService;

import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService service;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService service, EventService eventService, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model) {

        Sort sortObj = dir.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending();
            List<AppUser> users = service.listAllUsers(sortObj)
            .stream()
            .collect(Collectors.toList());

        model.addAttribute("tab", "dashboard");
        model.addAttribute("users", users); 
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);

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
        service.updateUser(cuser);
        return "redirect:/admin/dashboard";
    }
    @GetMapping("/edit/admin/password/{id}")
    public String editPassordForm(@PathVariable Long id, Model model) {
        AppUser user = service.findById(id);
        model.addAttribute("user", user);
        return "admin/password";
    }
    
    @PostMapping("/edit/admin/password")
    public String updateUserPassword(@ModelAttribute("user") AppUser cuser) {
        AppUser existing = service.findById(cuser.getId());
        String raw = cuser.getPassword();
        if (raw != null && !raw.isBlank()) {
            existing.setPassword(passwordEncoder.encode(raw));
            service.updateUser(existing);
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return "redirect:/admin/dashboard";
    }
}
