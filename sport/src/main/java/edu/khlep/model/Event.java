package edu.khlep.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import edu.khlep.model.Event.EventStatus;
import edu.khlep.model.Event.EventVenueType;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "place", nullable = false)
    private String place;

    @Enumerated(EnumType.STRING)
    @Column(name = "venue_type", nullable = false)
    private EventVenueType venueType;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "starts_at", nullable = false)
    private OffsetDateTime startsAt;

    @Column(name = "ends_at", nullable = false)
    private OffsetDateTime endsAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",     nullable = false)
    private EventStatus status;


    @Column(name = "current_participants_count", nullable = false)
    private Integer currentParticipantsCount = 0;

    @ManyToMany
    @JoinTable(
      name = "event_participants",
      joinColumns = @JoinColumn(name = "event_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<AppUser> participants = new HashSet<>();

    public Event() {}

    public Event(String name,
                 String description,
                 String place,
                 EventVenueType venueType,
                 Integer maxParticipants,
                 OffsetDateTime startsAt,
                 OffsetDateTime endsAt) {
        this.name = name;
        this.description = description;
        this.place = place;
        this.venueType = venueType;
        this.maxParticipants = maxParticipants;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public EventVenueType getVenueType() { return venueType; }
    public void setVenueType(EventVenueType venueType) { this.venueType = venueType; }

    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }

    public OffsetDateTime getStartsAt() { return startsAt; }
    public void setStartsAt(OffsetDateTime startsAt) { this.startsAt = startsAt; }

    public OffsetDateTime getEndsAt() { return endsAt; }
    public void setEndsAt(OffsetDateTime endsAt) { this.endsAt = endsAt; }

    public EventStatus getStatus() { return status; }
    public void setStatus(EventStatus status) { this.status = status; }

    public Integer getCurrentParticipantsCount() { return currentParticipantsCount; }
    public void setCurrentParticipantsCount(Integer count) { this.currentParticipantsCount = count; }

    public Set<AppUser> getParticipants() { return participants; }
    public void setParticipants(Set<AppUser> participants) {
        this.participants = participants;
        this.currentParticipantsCount = participants.size();
    }

    public void addParticipant(AppUser user) {
        participants.add(user);
        this.currentParticipantsCount = participants.size();
    }

    public void removeParticipant(AppUser user) {
        participants.remove(user);
        this.currentParticipantsCount = participants.size();
    }

    @Override
    public String toString() {
        return "Event{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", place='" + place + '\'' +
               ", startsAt=" + startsAt +
               ", endsAt=" + endsAt +
               ", status=" + status +
               ", currentParticipantsCount=" + currentParticipantsCount +
               '}';
    }


    public enum EventVenueType {
        indoors,
        outdoors
    }

    public enum EventStatus {
        active_registration("Active Registration"),
        registration_closed  ("Registration Closed"),
        archived             ("Archived");

        private final String displayName;
        EventStatus(String displayName) {
            this.displayName = displayName;
        }
        public String getDisplayName() {
            return displayName;
        }
    }


    public Boolean IsOpen() {
        if (status != EventStatus.active_registration) {
            return false;
        }
        return true;
    }

}

