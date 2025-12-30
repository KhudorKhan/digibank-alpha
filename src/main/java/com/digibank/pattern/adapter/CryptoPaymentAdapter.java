package com.digibank.pattern.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Adapter Pattern: Interface for crypto payment gateways
 * Allows integration with different blockchain networks
 */
public interface CryptoPaymentAdapter {
    String processPayment(String walletAddress, BigDecimal amount, String network);
    boolean verifyTransaction(String transactionHash, String network);
    String getNetworkName();
}

/**
 * Ethereum Adapter
 */
class EthereumAdapter implements CryptoPaymentAdapter {
    private static final Logger log = LoggerFactory.getLogger(EthereumAdapter.class);
    @Override
    public String processPayment(String walletAddress, BigDecimal amount, String network) {
        log.info("Processing Ethereum payment: {} ETH to {}", amount, walletAddress);
        // Simulated transaction hash
        return "0x" + System.currentTimeMillis() + "eth";
    }

    @Override
    public boolean verifyTransaction(String transactionHash, String network) {
        log.info("Verifying Ethereum transaction: {}", transactionHash);
        return transactionHash != null && transactionHash.startsWith("0x");
    }

    @Override
    public String getNetworkName() {
        return "ETHEREUM";
    }
}

/**
 * Bitcoin Adapter
 */
class BitcoinAdapter implements CryptoPaymentAdapter {
    private static final Logger log = LoggerFactory.getLogger(BitcoinAdapter.class);
    @Override
    public String processPayment(String walletAddress, BigDecimal amount, String network) {
        log.info("Processing Bitcoin payment: {} BTC to {}", amount, walletAddress);
        // Simulated transaction hash
        return "btc_" + System.currentTimeMillis();
    }

    @Override
    public boolean verifyTransaction(String transactionHash, String network) {
        log.info("Verifying Bitcoin transaction: {}", transactionHash);
        return transactionHash != null && transactionHash.startsWith("btc_");
    }

    @Override
    public String getNetworkName() {
        return "BITCOIN";
    }
}

/**
 * Polygon Adapter
 */
class PolygonAdapter implements CryptoPaymentAdapter {
    private static final Logger log = LoggerFactory.getLogger(PolygonAdapter.class);
    @Override
    public String processPayment(String walletAddress, BigDecimal amount, String network) {
        log.info("Processing Polygon payment: {} MATIC to {}", amount, walletAddress);
        // Simulated transaction hash
        return "0x" + System.currentTimeMillis() + "poly";
    }

    @Override
    public boolean verifyTransaction(String transactionHash, String network) {
        log.info("Verifying Polygon transaction: {}", transactionHash);
        return transactionHash != null && transactionHash.contains("poly");
    }

    @Override
    public String getNetworkName() {
        return "POLYGON";
    }
}

