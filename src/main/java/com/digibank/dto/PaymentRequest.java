package com.digibank.dto;

import com.digibank.model.Transaction;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Transaction.PaymentType paymentType;
    private BigDecimal amount;
    private Transaction.ServiceType serviceType;
    private String description;
    private String cryptoNetwork; // Required for crypto payments
}


