package edu.khlep.service;

import edu.khlep.model.AppUser;
import edu.khlep.repository.UserRepository;

import java.util.List;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public AppUser SignUpNewUser(AppUser user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        return userRepository.save(user);
    }
    public List<AppUser> findAll() {
        return userRepository.findAll();
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