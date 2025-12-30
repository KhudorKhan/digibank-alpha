package com.digibank.controller;

import com.digibank.dto.ApiResponse;
import com.digibank.model.Account;
import com.digibank.model.User;
import com.digibank.service.AccountService;
import com.digibank.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthService authService;

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<Account>> getBalance(
            @RequestHeader("Authorization") String token) {
        try {
            User user = authService.validateToken(token.replace("Bearer ", ""));
            Account account = accountService.getAccountByUserId(user.getId());
            return ResponseEntity.ok(ApiResponse.success(account));
        } catch (Exception e) {
            log.error("Failed to fetch balance: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}


