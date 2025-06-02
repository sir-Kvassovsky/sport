package edu.khlep.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import edu.khlep.model.AppUser;
import edu.khlep.model.Event;
import edu.khlep.service.EventService;
import edu.khlep.service.UserService;

@Controller
public class MainController {

    private final EventService eventService;
    private final UserService  userService;

    public MainController(EventService eventService,
                          UserService userService) {
        this.eventService = eventService;
        this.userService  = userService;
    }


    @GetMapping("/")
    public String home() {
        return "redirect:/main";
    } 
    
    @GetMapping("/main")
    public String main(
            @RequestParam(defaultValue = "startsAt") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model) {

        Sort sortObj = dir.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending();
            List<Event> events = eventService
            .listAllEvents(sortObj)
            .stream()
            .filter(event -> event.getStatus() != Event.EventStatus.archived)
            .collect(Collectors.toList());

        model.addAttribute("tab", "main");

        model.addAttribute("events", events);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);

        Set<Long> subscribedEventIds = Collections.emptySet();
        AppUser current = userService.getCurrentUser();
        if (current != null) {
            subscribedEventIds = eventService
                .getEventsForCurrentUser()
                .stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
        }
        model.addAttribute("subscribedEventIds", subscribedEventIds);

        return "event/main";
    }
    
    
    @GetMapping("/archive")
    public String archive(
            @RequestParam(defaultValue = "startsAt") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model) 
    {

        Sort sortObj = dir.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending();
        List<Event> events = eventService
            .listAllEvents(sortObj)
            .stream()
            .filter(event -> event.getStatus() == Event.EventStatus.archived)
            .collect(Collectors.toList());
    
        model.addAttribute("events", events);
        model.addAttribute("tab", "archive");
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);

        Set<Long> subscribedEventIds = Collections.emptySet();
        AppUser current = userService.getCurrentUser();
        if (current != null) {
            subscribedEventIds = eventService
                .getEventsForCurrentUser()
                .stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
        }
        model.addAttribute("subscribedEventIds", subscribedEventIds);
    
        return "event/archive";
    }


    @GetMapping("/about")
    public String aboutAuthor(Model model){
        return "event/about";
    }

    @GetMapping("/event/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);
        return "event/event";
    }
}
