package com.digibank.pattern.observer;

import com.digibank.model.Transaction;
import com.digibank.service.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Concrete Observer: Security alert service
 */
@Component
public class SecurityAlertObserver implements NotificationObserver {
    private static final Logger log = LoggerFactory.getLogger(SecurityAlertObserver.class);
    @Autowired
    private AuditService auditService;

    @Override
    public void update(Transaction transaction) {
        if (transaction.getStatus() == Transaction.TransactionStatus.FAILED) {
            String alert = String.format(
                "SECURITY ALERT: Failed transaction %s for user %s - Amount: %s",
                transaction.getId(),
                transaction.getUser().getUsername(),
                transaction.getAmount()
            );
            log.warn("🚨 {}", alert);
            auditService.logSecurityEvent(transaction.getUser().getId(), alert);
        }
    }

    @Override
    public String getObserverType() {
        return "SECURITY_ALERT";
    }
}


