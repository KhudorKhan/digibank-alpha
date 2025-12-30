package com.digibank.controller;

import com.digibank.dto.ApiResponse;
import com.digibank.model.Transaction;
import com.digibank.model.User;
import com.digibank.repository.TransactionRepository;
import com.digibank.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Transaction>>> getTransactions(
            @RequestHeader("Authorization") String token) {
        try {
            User user = authService.validateToken(token.replace("Bearer ", ""));
            List<Transaction> transactions = transactionRepository.findByUser(user);
            return ResponseEntity.ok(ApiResponse.success(transactions));
        } catch (Exception e) {
            log.error("Failed to fetch transactions: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Transaction>> getTransaction(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        try {
            User user = authService.validateToken(token.replace("Bearer ", ""));
            Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

            // Verify transaction belongs to user
            if (!transaction.getUser().getId().equals(user.getId()) && 
                user.getRole() != User.UserRole.ADMIN) {
                return ResponseEntity.status(403)
                    .body(ApiResponse.error("Access denied"));
            }

            return ResponseEntity.ok(ApiResponse.success(transaction));
        } catch (Exception e) {
            log.error("Failed to fetch transaction: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}


