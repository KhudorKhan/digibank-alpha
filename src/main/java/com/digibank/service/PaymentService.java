package com.digibank.service;

import com.digibank.model.Account;
import com.digibank.model.Transaction;
import com.digibank.model.User;
import com.digibank.pattern.adapter.CryptoAdapterFactory;
import com.digibank.pattern.adapter.CryptoPaymentAdapter;
import com.digibank.pattern.observer.NotificationSubject;
import com.digibank.pattern.singleton.CityController;
import com.digibank.pattern.template.PaymentSecurityCheck;
import com.digibank.repository.AccountRepository;
import com.digibank.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private NotificationSubject notificationSubject;

    @Autowired
    private PaymentSecurityCheck securityCheck;

    @Autowired
    private AuditService auditService;

    @Transactional
    public Transaction processFiatPayment(User user, Account account, BigDecimal amount,
                                         Transaction.ServiceType serviceType, String description) {
        // Template Method: Security check
        if (!securityCheck.performSecurityCheck(user, amount)) {
            throw new RuntimeException("Security check failed");
        }

        if (account.getFiatBalance().compareTo(amount) < 0) {
            Transaction failedTx = createFailedTransaction(user, Transaction.PaymentType.FIAT, amount, serviceType, description);
            notificationSubject.notifyObservers(failedTx);
            throw new RuntimeException("Insufficient fiat balance");
        }

        account.setFiatBalance(account.getFiatBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setPaymentType(Transaction.PaymentType.FIAT);
        transaction.setServiceType(serviceType);
        transaction.setAmount(amount);
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        transaction.setDescription(description);

        Transaction savedTx = transactionRepository.save(transaction);

        // Singleton: Update city controller metrics
        CityController.getInstance().incrementTransactionCount();
        CityController.getInstance().addRevenue(amount.longValue());

        // Observer: Notify all observers
        notificationSubject.notifyObservers(savedTx);

        // Audit logging
        auditService.logPayment(user.getId(), "FIAT", amount, serviceType);

        log.info("Fiat payment processed: {} for user {}", amount, user.getUsername());
        return savedTx;
    }

    @Transactional
    public Transaction processCryptoPayment(User user, Account account, BigDecimal amount,
                                          String cryptoNetwork, Transaction.ServiceType serviceType, String description) {
        // Template Method: Security check
        if (!securityCheck.performSecurityCheck(user, amount)) {
            throw new RuntimeException("Security check failed");
        }

        if (account.getCryptoBalance().compareTo(amount) < 0) {
            Transaction failedTx = createFailedTransaction(user, Transaction.PaymentType.CRYPTO, amount, serviceType, description);
            notificationSubject.notifyObservers(failedTx);
            throw new RuntimeException("Insufficient crypto balance");
        }

        // Adapter Pattern: Use appropriate crypto adapter
        CryptoPaymentAdapter adapter = CryptoAdapterFactory.getAdapter(cryptoNetwork);
        String walletAddress = "user_" + user.getId() + "_wallet";
        String transactionHash = adapter.processPayment(walletAddress, amount, cryptoNetwork);

        account.setCryptoBalance(account.getCryptoBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setPaymentType(Transaction.PaymentType.CRYPTO);
        transaction.setServiceType(serviceType);
        transaction.setAmount(amount);
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        transaction.setCryptoNetwork(cryptoNetwork);
        transaction.setTransactionHash(transactionHash);
        transaction.setDescription(description);

        Transaction savedTx = transactionRepository.save(transaction);

        // Singleton: Update city controller metrics
        CityController.getInstance().incrementTransactionCount();
        CityController.getInstance().addRevenue(amount.longValue());

        // Observer: Notify all observers
        notificationSubject.notifyObservers(savedTx);

        // Audit logging
        auditService.logPayment(user.getId(), "CRYPTO", amount, serviceType);

        log.info("Crypto payment processed: {} {} for user {}", amount, cryptoNetwork, user.getUsername());
        return savedTx;
    }

    private Transaction createFailedTransaction(User user, Transaction.PaymentType paymentType,
                                               BigDecimal amount, Transaction.ServiceType serviceType, String description) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setPaymentType(paymentType);
        transaction.setServiceType(serviceType);
        transaction.setAmount(amount);
        transaction.setStatus(Transaction.TransactionStatus.FAILED);
        transaction.setDescription(description);
        return transactionRepository.save(transaction);
    }

    public void refundFiatPayment(User user, Account account, BigDecimal amount) {
        account.setFiatBalance(account.getFiatBalance().add(amount));
        accountRepository.save(account);
        log.info("Refunded {} fiat to user {}", amount, user.getUsername());
    }

    public void logRefundAttempt(User user, BigDecimal amount, String network) {
        log.warn("Crypto refund attempt logged for user {}: {} {}", user.getUsername(), amount, network);
    }
}


