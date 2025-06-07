package edu.khlep.controller;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import edu.khlep.model.Event;
import edu.khlep.service.EventService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final EventService service;
    private static final ZoneId MOSCOW_ZONE = ZoneId.of("Europe/Moscow");
    private static final DateTimeFormatter DT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public ManagerController(EventService service) {
        this.service = service;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Event event = service.getEventById(id);
        OffsetDateTime startsOffset = event.getStartsAt();
        if (startsOffset != null) {
            ZonedDateTime zonedStarts = startsOffset.atZoneSameInstant(MOSCOW_ZONE);
            LocalDateTime localStarts = zonedStarts.toLocalDateTime();
            model.addAttribute("startsAtFormatted", localStarts.format(DT_FORMATTER));
        } else {
            model.addAttribute("startsAtFormatted", "");
        }

        OffsetDateTime endsOffset = event.getEndsAt();
        if (endsOffset != null) {
            ZonedDateTime zonedEnds = endsOffset.atZoneSameInstant(MOSCOW_ZONE);
            LocalDateTime localEnds = zonedEnds.toLocalDateTime();
            model.addAttribute("endsAtFormatted", localEnds.format(DT_FORMATTER));
        } else {
            model.addAttribute("endsAtFormatted", "");
        }

        model.addAttribute("event", event);
        return "manager/edit";
    }

    @PostMapping("/edit")
    public String updateEvent(@ModelAttribute("event") Event event) {
        service.updateEvent(event);
        return "redirect:/main";
    }

    @PostMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
        return "redirect:/main";
    }

    @GetMapping("/new")
    public String newEvent(Model model) {
        model.addAttribute("event", new Event());
        return "manager/new";
    }
    
    @PostMapping("/add")
    public String newEventAdded(@ModelAttribute("event") Event event) {
        try {
            service.createEvent(event);
        } catch (Exception e) {
            return "manager/new";
        }
        return "redirect:/main?created";
    }
}
