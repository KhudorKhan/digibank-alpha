package com.digibank.service;

import com.digibank.model.User;
import com.digibank.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public String login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }

        User user = userOpt.get();
        // In production, use BCrypt or similar for password hashing
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        log.info("User {} logged in successfully", username);
        return jwtService.generateToken(user);
    }

    public User register(String username, String email, String password, User.UserRole role) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password); // Should be hashed in production
        user.setRole(role != null ? role : User.UserRole.RESIDENT);

        User savedUser = userRepository.save(user);
        log.info("User {} registered successfully", username);
        return savedUser;
    }

    public User validateToken(String token) {
        String username = jwtService.extractUsername(token);
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Invalid token"));
    }
}


