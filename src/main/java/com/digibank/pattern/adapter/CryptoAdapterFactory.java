package com.digibank.pattern.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter Factory
 */
public class CryptoAdapterFactory {
    private static final Logger log = LoggerFactory.getLogger(CryptoAdapterFactory.class);
    
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


