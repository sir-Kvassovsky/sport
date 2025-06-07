package edu.khlep.service;

import edu.khlep.model.AppUser;
import edu.khlep.repository.UserRepository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("AppUser not found"));
        
        return User.withUsername(user.getUsername())
                   .password(user.getPassword())
                   .roles(user.getRole().replace("ROLE_", ""))
                   .build();
    }
    public AppUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        String username = auth.getName();
        return userRepository.findByUsername(username).orElse(null);
    }
    public AppUser SignUpNewUser(AppUser user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("PARTICIPANT");
        }
        return userRepository.save(user);
    }
    public List<AppUser> findAll() {
        return userRepository.findAll();
    }
    @Transactional(readOnly = true)
    public long countAllUsers() {
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    public long countParticipants() {
        return userRepository.countByRole("PARTICIPANT");
    }

    public List<AppUser> listAllUsers(Sort sort) {
        return userRepository.findAll(sort);
    }
    
    public AppUser addUser(AppUser user) {
        return userRepository.save(user);
    }
    
    public AppUser updateUser(AppUser user) {
        return userRepository.save(user);  
    }
    
    public AppUser findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}