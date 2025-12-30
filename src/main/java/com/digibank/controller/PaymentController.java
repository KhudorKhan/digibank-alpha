package com.digibank.controller;

import com.digibank.dto.ApiResponse;
import com.digibank.dto.PaymentRequest;
import com.digibank.model.Account;
import com.digibank.model.Transaction;
import com.digibank.model.User;
import com.digibank.pattern.command.CryptoPaymentCommand;
import com.digibank.pattern.command.FiatPaymentCommand;
import com.digibank.pattern.command.PaymentCommand;
import com.digibank.service.AccountService;
import com.digibank.service.AuthService;
import com.digibank.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pay")
@Slf4j
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthService authService;

    @PostMapping("/fiat")
    public ResponseEntity<ApiResponse<Transaction>> payFiat(
            @RequestHeader("Authorization") String token,
            @RequestBody PaymentRequest request) {
        try {
            User user = authService.validateToken(token.replace("Bearer ", ""));
            Account account = accountService.getAccountByUserId(user.getId());

            // Command Pattern: Create and execute fiat payment command
            PaymentCommand command = new FiatPaymentCommand(
                paymentService, user, account, request.getAmount(),
                request.getServiceType(), request.getDescription()
            );

            if (!command.canExecute()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Insufficient balance"));
            }

            Transaction transaction = command.execute();
            log.info("Fiat payment executed: {} for user {}", request.getAmount(), user.getUsername());
            return ResponseEntity.ok(ApiResponse.success("Payment processed", transaction));
        } catch (Exception e) {
            log.error("Fiat payment failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/crypto")
    public ResponseEntity<ApiResponse<Transaction>> payCrypto(
            @RequestHeader("Authorization") String token,
            @RequestBody PaymentRequest request) {
        try {
            if (request.getCryptoNetwork() == null || request.getCryptoNetwork().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Crypto network is required"));
            }

            User user = authService.validateToken(token.replace("Bearer ", ""));
            Account account = accountService.getAccountByUserId(user.getId());

            // Command Pattern: Create and execute crypto payment command
            PaymentCommand command = new CryptoPaymentCommand(
                paymentService, user, account, request.getAmount(),
                request.getCryptoNetwork(), request.getServiceType(), request.getDescription()
            );

            if (!command.canExecute()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Insufficient crypto balance"));
            }

            Transaction transaction = command.execute();
            log.info("Crypto payment executed: {} {} for user {}", 
                request.getAmount(), request.getCryptoNetwork(), user.getUsername());
            return ResponseEntity.ok(ApiResponse.success("Crypto payment processed", transaction));
        } catch (Exception e) {
            log.error("Crypto payment failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}


