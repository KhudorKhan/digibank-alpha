package com.digibank.pattern.command;

import com.digibank.model.Account;
import com.digibank.model.Transaction;
import com.digibank.model.User;
import com.digibank.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Concrete Command: Fiat payment implementation
 */
@Data
@AllArgsConstructor
public class FiatPaymentCommand implements PaymentCommand {
    private PaymentService paymentService;
    private User user;
    private Account account;
    private BigDecimal amount;
    private Transaction.ServiceType serviceType;
    private String description;

    @Override
    public Transaction execute() {
        if (!canExecute()) {
            throw new IllegalStateException("Payment cannot be executed: insufficient balance");
        }
        return paymentService.processFiatPayment(user, account, amount, serviceType, description);
    }

    @Override
    public void undo() {
        // Refund logic would go here
        paymentService.refundFiatPayment(user, account, amount);
    }

    @Override
    public boolean canExecute() {
        return account.getFiatBalance().compareTo(amount) >= 0;
    }
}


