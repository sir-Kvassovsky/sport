package edu.khlep.controller;

import java.util.List;

import edu.khlep.model.AppUser;
import edu.khlep.service.EventService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    private EventService service;

    public ManagerController(EventService service) {
        this.service = service;
    }
    // @GetMapping("/edit/{id}")
    // public String editForm(@PathVariable Long id, Model model) {
    //     AppUser user = service.findById(id);
    //     model.addAttribute("user", user);
    //     return "admin/edit_user";
    // }

    // @PostMapping("/edit")
    // public String updateUser(@ModelAttribute("user") AppUser cuser) {
    //     AppUser existing = service.findById(cuser.getId());
    //     String raw = cuser.getPassword();
    //     if (raw != null && !raw.isBlank()) {
    //         existing.setPassword(passwordEncoder.encode(raw));
    //         service.updateUser(existing);
    //     }
    //     else{
    //         service.updateUser(cuser);
    //     }
    //     return "redirect:/admin/dashboard";
    // }

    @PostMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
        return "redirect:/main";
    }
}
