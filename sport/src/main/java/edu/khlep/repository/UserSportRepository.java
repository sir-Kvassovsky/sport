package edu.khlep.repository;

import edu.khlep.model.EventParticipant;
import edu.khlep.model.AppUser;
import edu.khlep.model.Event;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import jakarta.transaction.Transactional;

@Repository
public interface UserSportRepository
        extends JpaRepository<EventParticipant, EventParticipant.EventParticipantId> {

    List<EventParticipant> findAllByUser(AppUser user);
    

    @Modifying
    @Transactional
    @Query("DELETE FROM EventParticipant ep WHERE ep.id.eventId = :eventId AND ep.id.userId = :userId")
    void deleteByEventIdAndUserId(@Param("eventId") Long eventId, @Param("userId") Long userId);


    long countByEvent(Event event);
}
