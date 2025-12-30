package com.digibank.pattern.template;

import com.digibank.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Template Method Pattern: Defines skeleton of security check algorithm
 * Subclasses implement specific steps
 */
public abstract class SecurityCheckTemplate {
    private static final Logger log = LoggerFactory.getLogger(SecurityCheckTemplate.class);
    /**
     * Template method - defines the algorithm structure
     */
    public final boolean performSecurityCheck(User user, BigDecimal amount) {
        log.info("Starting security check for user: {}", user.getUsername());
        
        boolean step1 = validateUser(user);
        if (!step1) {
            logSecurityFailure("User validation failed", user);
            return false;
        }

        boolean step2 = checkAccountStatus(user);
        if (!step2) {
            logSecurityFailure("Account status check failed", user);
            return false;
        }

        boolean step3 = validateAmount(amount);
        if (!step3) {
            logSecurityFailure("Amount validation failed", user);
            return false;
        }

        boolean step4 = performCustomChecks(user, amount);
        if (!step4) {
            logSecurityFailure("Custom security checks failed", user);
            return false;
        }

        log.info("Security check passed for user: {}", user.getUsername());
        return true;
    }

    // Abstract methods to be implemented by subclasses
    protected abstract boolean validateUser(User user);
    protected abstract boolean checkAccountStatus(User user);
    protected abstract boolean validateAmount(BigDecimal amount);
    protected abstract boolean performCustomChecks(User user, BigDecimal amount);

    // Hook method - can be overridden
    protected void logSecurityFailure(String reason, User user) {
        log.warn("Security check failed: {} for user: {}", reason, user.getUsername());
    }
}


