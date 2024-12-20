package com.example.pharmacyappjvfx.controllers;


import com.example.pharmacyappjvfx.services.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AuthController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    private AuthService authService = AuthService.getInstance();
    private final SceneSwitcher sceneSwitcher = SceneSwitcher.getInstance();

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Пожалуйста, заполните все поля");
            return;
        }

        try {
            String responseMessage = authService.login(username, password);

            if (responseMessage.startsWith("Ошибка") || responseMessage.startsWith("Неверное")) {

                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText(responseMessage);
            } else {
                statusLabel.setStyle("-fx-text-fill: green;");
                statusLabel.setText("Вход выполнен: " + responseMessage);

                sceneSwitcher.switchTo("/views/medicines.fxml", false);

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Ошибка соединения с сервером");
        }
    }
}


