package com.digibank.pattern.observer;

import com.digibank.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern: Subject that notifies all observers
 */
@Component
public class NotificationSubject {
    private static final Logger log = LoggerFactory.getLogger(NotificationSubject.class);
    private final List<NotificationObserver> observers = new ArrayList<>();

    @Autowired
    public NotificationSubject(
            EmailNotificationObserver emailObserver,
            SecurityAlertObserver securityObserver) {
        registerObserver(emailObserver);
        registerObserver(securityObserver);
    }

    public void registerObserver(NotificationObserver observer) {
        observers.add(observer);
        log.debug("Registered observer: {}", observer.getObserverType());
    }

    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Transaction transaction) {
        log.info("Notifying {} observers about transaction {}", observers.size(), transaction.getId());
        for (NotificationObserver observer : observers) {
            observer.update(transaction);
        }
    }
}


