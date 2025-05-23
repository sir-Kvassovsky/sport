package edu.khlep.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "event_participants")
public class EventParticipant {

    @EmbeddedId
    private EventParticipantId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("eventId")
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "joined_at", nullable = false)
    private OffsetDateTime joinedAt = OffsetDateTime.now();

    public EventParticipant() {}

    public EventParticipant(Event event, AppUser user) {
        this.event = event; 
        this.user  = user;
        this.id    = new EventParticipantId(event.getId(), user.getId());
    }

    public EventParticipantId getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public AppUser getUser() {
        return user;
    }

    public OffsetDateTime getJoinedAt() {
        return joinedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventParticipant that = (EventParticipant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Embeddable
    public static class EventParticipantId implements Serializable {
        @Column(name = "event_id")
        private Long eventId;

        @Column(name = "user_id")
        private Long userId;

        public EventParticipantId() {}

        public EventParticipantId(Long eventId, Long userId) {
            this.eventId = eventId;
            this.userId  = userId;
        }

        public Long getEventId() {
            return eventId;
        }

        public Long getUserId() {
            return userId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EventParticipantId)) return false;
            EventParticipantId that = (EventParticipantId) o;
            return Objects.equals(eventId, that.eventId) &&
                   Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(eventId, userId);
        }
    }
}
