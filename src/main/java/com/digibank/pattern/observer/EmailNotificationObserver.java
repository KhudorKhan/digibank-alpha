package com.digibank.pattern.observer;

import com.digibank.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Concrete Observer: Email notification service
 */
@Component
@Slf4j
public class EmailNotificationObserver implements NotificationObserver {
    @Override
    public void update(Transaction transaction) {
        String message = String.format(
            "Transaction %s: %s payment of %s for %s - Status: %s",
            transaction.getId(),
            transaction.getPaymentType(),
            transaction.getAmount(),
            transaction.getServiceType(),
            transaction.getStatus()
        );
        log.info("📧 Email Notification: {}", message);
        // In production, would send actual email via SMTP
    }

    @Override
    public String getObserverType() {
        return "EMAIL";
    }
}


