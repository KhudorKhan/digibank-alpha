package com.digibank.pattern.command;

import com.digibank.model.Transaction;

/**
 * Command Pattern: Encapsulates payment requests as objects
 * Allows parameterization, queuing, logging, and undo operations
 */
public interface PaymentCommand {
    /**
     * Execute the payment command
     * @return Transaction object representing the executed payment
     */
    Transaction execute();

    /**
     * Undo the payment (if supported)
     */
    void undo();

    /**
     * Check if command can be executed
     */
    boolean canExecute();
}


