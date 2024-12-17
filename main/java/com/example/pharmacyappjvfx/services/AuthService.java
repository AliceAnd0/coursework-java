package com.example.pharmacyappjvfx.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthService {

    private static final AuthService instance = new AuthService();
    private final HttpClient httpClient = HttpClientManager.getInstance();
    private AuthManager authManager = AuthManager.getInstance();

    private AuthService() {
    }

    public static AuthService getInstance() {
        return instance;
    }

    public String login(String username, String password) throws IOException, InterruptedException {
        String basicAuth = Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/auth/login"))
                .header("Authorization", "Basic " + basicAuth)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            authManager.setBasicAuth("Basic " + Base64.getEncoder()
                    .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)));
            authManager.setUsername(username);

            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);

            // Извлекаем роль и имя пользователя
            JsonArray rolesArray = jsonResponse.getAsJsonArray("roles");
            authManager.setRole(rolesArray.get(0).getAsString());

            return "Успешный вход";  // Успешный вход
        } else if (response.statusCode() == 401) {
            return "Неверное имя пользователя или пароль";  // Ошибка авторизации
        } else {
            return "Ошибка: " + response.statusCode();  // Другая ошибка
        }
    }
}
