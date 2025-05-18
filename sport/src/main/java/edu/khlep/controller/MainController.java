package edu.khlep.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "redirect:/main";
    } 
    
    @GetMapping("/main")
    public String Main(Model model) {
        return "guest/main";
    }

    @GetMapping("/about")
    public String aboutAuthor(Model model){
        return "guest/about";
    }
}
