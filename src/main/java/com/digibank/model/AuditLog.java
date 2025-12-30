package com.digibank.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Audit log entity for security and compliance tracking
 */
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditEventType eventType;

    @Column(nullable = false)
    private String action;

    @Column(length = 2000)
    private String details;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column
    private String ipAddress;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AuditEventType getEventType() {
        return eventType;
    }

    public void setEventType(AuditEventType eventType) {
        this.eventType = eventType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public enum AuditEventType {
        AUTHENTICATION,
        PAYMENT,
        SECURITY_ALERT,
        ADMIN_ACTION,
        SYSTEM_EVENT
    }
}


