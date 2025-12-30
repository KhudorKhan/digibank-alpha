package com.digibank.pattern.singleton;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Singleton Pattern: Central city controller managing system-wide state
 * Ensures only one instance exists throughout the application lifecycle
 */
@Component
@Getter
public class CityController {
    private static volatile CityController instance;
    private final AtomicLong totalTransactions = new AtomicLong(0);
    private final AtomicLong totalRevenue = new AtomicLong(0);
    private LocalDateTime systemStartTime;
    private boolean systemActive = true;

    private CityController() {
        systemStartTime = LocalDateTime.now();
    }

    /**
     * Initialize singleton instance when Spring creates the bean
     */
    @PostConstruct
    public void init() {
        if (instance == null) {
            synchronized (CityController.class) {
                if (instance == null) {
                    instance = this;
                }
            }
        }
    }

    /**
     * Thread-safe singleton instance retrieval
     * Demonstrates classic Singleton pattern
     */
    public static CityController getInstance() {
        if (instance == null) {
            synchronized (CityController.class) {
                if (instance == null) {
                    // In Spring context, this should not happen
                    // but provides fallback for non-Spring usage
                    instance = new CityController();
                }
            }
        }
        return instance;
    }

    public void incrementTransactionCount() {
        totalTransactions.incrementAndGet();
    }

    public void addRevenue(long amount) {
        totalRevenue.addAndGet(amount);
    }

    public long getTotalTransactions() {
        return totalTransactions.get();
    }

    public long getTotalRevenue() {
        return totalRevenue.get();
    }

    public boolean isSystemActive() {
        return systemActive;
    }

    public void setSystemActive(boolean active) {
        this.systemActive = active;
    }
}

