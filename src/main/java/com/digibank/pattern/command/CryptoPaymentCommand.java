package com.digibank.pattern.command;

import com.digibank.model.Account;
import com.digibank.model.Transaction;
import com.digibank.model.User;
import com.digibank.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Concrete Command: Crypto payment implementation
 */
@Data
@AllArgsConstructor
public class CryptoPaymentCommand implements PaymentCommand {
    private PaymentService paymentService;
    private User user;
    private Account account;
    private BigDecimal amount;
    private String cryptoNetwork;
    private Transaction.ServiceType serviceType;
    private String description;

    @Override
    public Transaction execute() {
        if (!canExecute()) {
            throw new IllegalStateException("Payment cannot be executed: insufficient balance");
        }
        return paymentService.processCryptoPayment(user, account, amount, cryptoNetwork, serviceType, description);
    }

    @Override
    public void undo() {
        // Crypto refunds are typically not reversible, but we log the attempt
        paymentService.logRefundAttempt(user, amount, cryptoNetwork);
    }

    @Override
    public boolean canExecute() {
        return account.getCryptoBalance().compareTo(amount) >= 0;
    }
}


