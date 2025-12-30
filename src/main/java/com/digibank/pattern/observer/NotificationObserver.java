package com.digibank.pattern.observer;

import com.digibank.model.Transaction;

/**
 * Observer Pattern: Interface for notification observers
 */
public interface NotificationObserver {
    void update(Transaction transaction);
    String getObserverType();
}


