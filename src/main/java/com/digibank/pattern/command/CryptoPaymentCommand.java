package com.digibank.pattern.command;

import com.digibank.model.Account;
import com.digibank.model.Transaction;
import com.digibank.model.User;
import com.digibank.service.PaymentService;

import java.math.BigDecimal;

/**
 * Concrete Command: Crypto payment implementation
 */
public class CryptoPaymentCommand implements PaymentCommand {
    private PaymentService paymentService;
    private User user;
    private Account account;
    private BigDecimal amount;
    private String cryptoNetwork;
    private Transaction.ServiceType serviceType;
    private String description;

    public CryptoPaymentCommand(PaymentService paymentService, User user, Account account,
                               BigDecimal amount, String cryptoNetwork, 
                               Transaction.ServiceType serviceType, String description) {
        this.paymentService = paymentService;
        this.user = user;
        this.account = account;
        this.amount = amount;
        this.cryptoNetwork = cryptoNetwork;
        this.serviceType = serviceType;
        this.description = description;
    }

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


