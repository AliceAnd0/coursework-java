package com.example.pharmacy2.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String username; // Логин

    @NotBlank
    @Size(min = 6, message = "Пароль должен содержать не менее 6 символов.")
    private String password; // Пароль (в хэшированном виде)

    @NotBlank
    private String role;
}