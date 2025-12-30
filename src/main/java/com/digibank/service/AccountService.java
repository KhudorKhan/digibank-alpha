package com.digibank.service;

import com.digibank.model.Account;
import com.digibank.model.User;
import com.digibank.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    @Autowired
    private AccountRepository accountRepository;

    public Account getOrCreateAccount(User user) {
        return accountRepository.findByUser(user)
            .orElseGet(() -> {
                Account account = new Account();
                account.setUser(user);
                account.setFiatBalance(BigDecimal.ZERO);
                account.setCryptoBalance(BigDecimal.ZERO);
                return accountRepository.save(account);
            });
    }

    public Account getAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Account addFiatBalance(Long userId, BigDecimal amount) {
        Account account = getAccountByUserId(userId);
        account.setFiatBalance(account.getFiatBalance().add(amount));
        return accountRepository.save(account);
    }

    public Account addCryptoBalance(Long userId, BigDecimal amount) {
        Account account = getAccountByUserId(userId);
        account.setCryptoBalance(account.getCryptoBalance().add(amount));
        return accountRepository.save(account);
    }
}


