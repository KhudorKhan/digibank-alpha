package com.digibank.pattern.template;

import com.digibank.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Concrete Template Method: Payment-specific security checks
 */
@Component
public class PaymentSecurityCheck extends SecurityCheckTemplate {
    private static final Logger log = LoggerFactory.getLogger(PaymentSecurityCheck.class);
    private static final BigDecimal MAX_TRANSACTION_AMOUNT = new BigDecimal("10000");
    private static final BigDecimal MIN_TRANSACTION_AMOUNT = new BigDecimal("0.01");

    @Override
    protected boolean validateUser(User user) {
        return user != null && user.getUsername() != null && !user.getUsername().isEmpty();
    }

    @Override
    protected boolean checkAccountStatus(User user) {
        // Check if user account is active (simplified)
        return user.getRole() != null;
    }

    @Override
    protected boolean validateAmount(BigDecimal amount) {
        if (amount == null) {
            return false;
        }
        return amount.compareTo(MIN_TRANSACTION_AMOUNT) >= 0 
            && amount.compareTo(MAX_TRANSACTION_AMOUNT) <= 0;
    }

    @Override
    protected boolean performCustomChecks(User user, BigDecimal amount) {
        // Additional checks: rate limiting, suspicious activity, etc.
        // For now, just check if amount is reasonable
        log.debug("Performing custom security checks for user: {}", user.getUsername());
        return true;
    }
}


