package edu.khlep.controller;

import java.util.List;

import edu.khlep.model.AppUser;
// import edu.khlep.model.Employee;
// import edu.khlep.service.EmployeeService;
import edu.khlep.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    // private final EmployeeService service;
    private final UserService userService;
 
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    } 
    
    @GetMapping("/main")
    public String main() {
        return "main"; 
    } 
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new AppUser());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") AppUser user) {
        try {
            userService.registerNewUser(user);
        } catch (Exception e) {
            // Если регистрация не удалась, можно добавить обработку ошибки и возвращать на страницу регистрации
            return "register";
        }
        return "redirect:/login?registered";
    }
    @GetMapping("/login")
    public String login() {
        return "login"; // возвращает шаблон login.html из папки templates
    }
}
