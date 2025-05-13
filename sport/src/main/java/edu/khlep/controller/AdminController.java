package edu.khlep.controller;

import java.util.List;

import edu.khlep.model.AppUser;
import edu.khlep.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService service;

    public AdminController(UserService service) {
        this.service = service;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        List<AppUser> users = service.findAll();
        model.addAttribute("users", users); 
        return "dashboard";
    }
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        AppUser user = service.findById(id);
        model.addAttribute("user", user);
        return "edit_user";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") AppUser user) {
        service.updateUser(user);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return "redirect:/admin/dashboard";
    }
}
