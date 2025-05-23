package edu.khlep.repository;

import edu.khlep.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SportEventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);
}