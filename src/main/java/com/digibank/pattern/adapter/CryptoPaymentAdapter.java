package com.digibank.pattern.adapter;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
class EthereumAdapter implements CryptoPaymentAdapter {
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
@Slf4j
class BitcoinAdapter implements CryptoPaymentAdapter {
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
@Slf4j
class PolygonAdapter implements CryptoPaymentAdapter {
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

/**
 * Adapter Factory
 */
@Slf4j
public class CryptoAdapterFactory {
    public static CryptoPaymentAdapter getAdapter(String network) {
        return switch (network.toUpperCase()) {
            case "ETH", "ETHEREUM" -> new EthereumAdapter();
            case "BTC", "BITCOIN" -> new BitcoinAdapter();
            case "MATIC", "POLYGON" -> new PolygonAdapter();
            default -> {
                log.warn("Unknown network {}, defaulting to Ethereum", network);
                yield new EthereumAdapter();
            }
        };
    }
}


