package edu.khlep.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.khlep.model.AppUser;
import edu.khlep.model.Event;
import edu.khlep.model.EventParticipant;
import edu.khlep.model.EventParticipant.EventParticipantId;
import edu.khlep.repository.SportEventRepository;
import edu.khlep.repository.UserSportRepository;

@Service
public class EventService {

    private final SportEventRepository             eventRepo;
    private final UserSportRepository  participantRepo;
    private final UserService              userService;

    public EventService(SportEventRepository eventRepo,
                        UserSportRepository participantRepo,
                        UserService  userService){
        this.eventRepo       = eventRepo;
        this.participantRepo = participantRepo;
        this.userService     = userService;
    }

    public Event createEvent(Event e) {
        return eventRepo.save(e);
    }

    public Event getEventById(Long id) {
        return eventRepo.findById(id).orElseThrow(
            () -> new RuntimeException("Event not found"));
    }

    public List<Event> listAllEvents() {
        return eventRepo.findAll();
    }

    public Event updateEvent(Event e) {
        return eventRepo.save(e);
    }

    public void deleteEvent(Long id) {
        eventRepo.deleteById(id);
    }

    @Transactional
    public void registerCurrentUserForEvent(Long eventId) {
        AppUser user = userService.getCurrentUser();
        Event event = getEventById(eventId);

        if (event.getStatus() != Event.EventStatus.active_registration) {
            throw new RuntimeException("Registration is closed");
        }

        EventParticipant.EventParticipantId id = new EventParticipant.EventParticipantId(eventId, user.getId());
        if (participantRepo.existsById(id)) {
            throw new RuntimeException("Already registered");
        }

        if (event.getCurrentParticipantsCount() >= event.getMaxParticipants()) {
            throw new RuntimeException("Event is full");
        }

        participantRepo.save(new EventParticipant(event, user));

        long count = participantRepo.countByEvent(event);
        event.setCurrentParticipantsCount((int) count);
        if (count >= event.getMaxParticipants()) {
            event.setStatus(Event.EventStatus.registration_closed);
        }
        eventRepo.save(event);
    }

    @Transactional
    public void unregisterCurrentUserFromEvent(Long eventId) {
        AppUser user = userService.getCurrentUser();
        Event event = getEventById(eventId);

        EventParticipant.EventParticipantId id = new EventParticipant.EventParticipantId(eventId, user.getId());
        if (!participantRepo.existsById(id)) {
            throw new RuntimeException("User is not registered for the event");
        }

        participantRepo.deleteById(id);

        long count = participantRepo.countByEvent(event);
        event.setCurrentParticipantsCount((int) count);
        event.setStatus(Event.EventStatus.active_registration);
        eventRepo.save(event);
    }


    public List<Event> getEventsForCurrentUser() {
        AppUser user = userService.getCurrentUser();
        return participantRepo.findAllByUser(user)
                            .stream()
                            .map(EventParticipant::getEvent)
                            .toList();
    }

    @Scheduled(cron = "0 */5 * * * *")   // every hour on the hour
    @Transactional
    public void closeEvents24HoursBeforeStart() {
        OffsetDateTime cutoff = OffsetDateTime.now().plusHours(24);
        List<Event> aboutToStart  = eventRepo.findByStatusAndStartsAtBefore(
            Event.EventStatus.active_registration, cutoff);

        for (Event e : aboutToStart) {
            e.setStatus(Event.EventStatus.registration_closed);
            eventRepo.save(e);
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void archivePastEvents() {
        OffsetDateTime now = OffsetDateTime.now();
        List<Event> finished = eventRepo.findByStatusNotAndEndsAtBefore(
            Event.EventStatus.archived, now);
        for (Event e : finished) {
            e.setStatus(Event.EventStatus.archived);
            eventRepo.save(e);
        }
    }

}
