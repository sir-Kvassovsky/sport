package edu.khlep.repository;

import edu.khlep.model.Event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface SportEventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);
    List<Event> findByStatusAndStartsAtBefore(Event.EventStatus status, OffsetDateTime dateTime);
    List<Event> findByStatusNotAndEndsAtBefore(Event.EventStatus status, OffsetDateTime dateTime);
    Page<Event> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
    
