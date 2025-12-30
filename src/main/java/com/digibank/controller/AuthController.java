package com.digibank.controller;

import com.digibank.dto.ApiResponse;
import com.digibank.dto.LoginRequest;
import com.digibank.dto.RegisterRequest;
import com.digibank.model.User;
import com.digibank.service.AccountService;
import com.digibank.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> register(@RequestBody RegisterRequest request) {
        try {
            User user = authService.register(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
            );

            // Create account for new user
            accountService.getOrCreateAccount(user);

            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getId());
            response.put("username", user.getUsername());
            response.put("role", user.getRole());

            log.info("User registered: {}", user.getUsername());
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", response));
        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest request,
                                                                  HttpServletRequest httpRequest) {
        try {
            String token = authService.login(request.getUsername(), request.getPassword());
            User user = authService.validateToken(token);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            response.put("userId", user.getId());

            log.info("User logged in: {}", user.getUsername());
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid credentials"));
        }
    }
}


