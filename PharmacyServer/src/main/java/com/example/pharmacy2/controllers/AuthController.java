package com.example.pharmacy2.controllers;

import com.example.pharmacy2.models.AppUser;
import com.example.pharmacy2.models.UserInfoDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        if (authentication != null) {
            // Извлекаем роли пользователя
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<String> roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            response.put("roles", roles);
            response.put("username", authentication.getName());
        }

        return ResponseEntity.ok(response);
        //return ResponseEntity.ok("Успешная авторизация");
    }
}