package com.example.pharmacyappjvfx.services;

import lombok.Data;

@Data
public class AuthManager {

    private static AuthManager instance;
    private String username;
    private String role;
    private String basicAuth;

    private AuthManager() {}

    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public boolean isAuthenticated() {
        return basicAuth != null && !basicAuth.isEmpty();
    }
}