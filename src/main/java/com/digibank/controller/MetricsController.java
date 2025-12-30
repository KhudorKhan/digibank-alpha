package com.digibank.controller;

import com.digibank.dto.ApiResponse;
import com.digibank.model.AuditLog;
import com.digibank.model.User;
import com.digibank.pattern.singleton.CityController;
import com.digibank.repository.AuditLogRepository;
import com.digibank.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {
    private static final Logger log = LoggerFactory.getLogger(MetricsController.class);
    @Autowired
    private AuthService authService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMetrics(
            @RequestHeader("Authorization") String token) {
        try {
            User user = authService.validateToken(token.replace("Bearer ", ""));
            
            // Only admins can access metrics
            if (user.getRole() != User.UserRole.ADMIN) {
                return ResponseEntity.status(403)
                    .body(ApiResponse.error("Access denied: Admin role required"));
            }

            // Singleton Pattern: Get metrics from CityController
            CityController controller = CityController.getInstance();
            
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("totalTransactions", controller.getTotalTransactions());
            metrics.put("totalRevenue", controller.getTotalRevenue());
            metrics.put("systemActive", controller.isSystemActive());
            metrics.put("systemStartTime", controller.getSystemStartTime());

            return ResponseEntity.ok(ApiResponse.success(metrics));
        } catch (Exception e) {
            log.error("Failed to fetch metrics: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getLogs(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) AuditLog.AuditEventType eventType) {
        try {
            User user = authService.validateToken(token.replace("Bearer ", ""));
            
            if (user.getRole() != User.UserRole.ADMIN) {
                return ResponseEntity.status(403)
                    .body(ApiResponse.error("Access denied: Admin role required"));
            }

            List<AuditLog> logs = eventType != null 
                ? auditLogRepository.findByEventType(eventType)
                : auditLogRepository.findAll();

            return ResponseEntity.ok(ApiResponse.success(logs));
        } catch (Exception e) {
            log.error("Failed to fetch logs: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}


