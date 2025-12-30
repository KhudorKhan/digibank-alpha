package com.digibank.pattern.observer;

import com.digibank.model.Transaction;
import com.digibank.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Concrete Observer: Security alert service
 */
@Component
@Slf4j
public class SecurityAlertObserver implements NotificationObserver {
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


