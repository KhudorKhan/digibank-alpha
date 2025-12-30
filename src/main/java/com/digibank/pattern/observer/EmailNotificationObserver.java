package com.digibank.pattern.observer;

import com.digibank.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Concrete Observer: Email notification service
 */
@Component
public class EmailNotificationObserver implements NotificationObserver {
    private static final Logger log = LoggerFactory.getLogger(EmailNotificationObserver.class);
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


