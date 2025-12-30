package com.digibank.dto;

import com.digibank.model.User;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private User.UserRole role;
}


