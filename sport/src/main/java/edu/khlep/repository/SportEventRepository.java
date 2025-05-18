package edu.khlep.repository;

import edu.khlep.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SportEventRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}