package com.digibank.dto;

import com.digibank.model.Transaction;

import java.math.BigDecimal;

public class PaymentRequest {
    private Transaction.PaymentType paymentType;
    private BigDecimal amount;
    private Transaction.ServiceType serviceType;
    private String description;
    private String cryptoNetwork; // Required for crypto payments

    public Transaction.PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Transaction.PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Transaction.ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(Transaction.ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCryptoNetwork() {
        return cryptoNetwork;
    }

    public void setCryptoNetwork(String cryptoNetwork) {
        this.cryptoNetwork = cryptoNetwork;
    }
}


