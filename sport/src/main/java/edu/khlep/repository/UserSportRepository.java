package edu.khlep.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.khlep.model.AppUser;

public interface UserSportRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}