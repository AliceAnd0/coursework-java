package com.example.pharmacyappjvfx.services;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Arrays;

import com.example.pharmacyappjvfx.models.Pharmacy;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PharmacyService {

    private static final PharmacyService instance = new PharmacyService();
    private final HttpClient httpClient = HttpClientManager.getInstance();
    private final String authHeader = AuthManager.getInstance().getBasicAuth();
    private final Gson gson = new Gson();

    private PharmacyService() {}

    public static PharmacyService getInstance() {
        return instance;
    }

    // Метод для загрузки всех аптек
    public ObservableList<Pharmacy> load() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/pharmacies/get_all"))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Pharmacy[] pharmacies = gson.fromJson(response.body(), Pharmacy[].class);
                return FXCollections.observableArrayList(pharmacies);
            } else {
                System.err.println("Ошибка загрузки аптек: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на загрузку аптек.");
        }
        return FXCollections.observableArrayList();
    }

    // Метод для обновления данных об аптеке
    public void update(Pharmacy pharmacy) {
        try {
            String jsonPharmacy = gson.toJson(pharmacy);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/pharmacies/update"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonPharmacy))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Информация об аптеке успешно обновлена: " + response.body());
            } else {
                System.err.println("Ошибка при обновлении информации об аптеке: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на обновление информации об аптеке.");
        }
    }

    // Метод для удаления аптеки
    public void delete(int pharmacyId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/pharmacies/delete/" + pharmacyId))
                    .header("Authorization", authHeader)
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Аптека успешно удалена: " + response.body());
            } else {
                System.err.println("Ошибка при удалении аптеки: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на удаление аптеки.");
        }
    }

    // Метод для добавления аптеки
    public void add(Pharmacy pharmacy) {
        try {
            String jsonPharmacy = gson.toJson(pharmacy);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/pharmacies/add"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPharmacy))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Аптека успешно добавлена: " + response.body());
            } else {
                System.err.println("Ошибка при добавлении аптеки: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на добавление аптеки.");
        }
    }

    // Метод для поиска аптек
    public ObservableList<Pharmacy> search(String filter) {
        try {
            String encodedFilter = URLEncoder.encode(filter, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/pharmacies/search/" + encodedFilter))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Pharmacy[] pharmacies = gson.fromJson(response.body(), Pharmacy[].class);
                return FXCollections.observableArrayList(pharmacies);
            } else {
                System.err.println("Ошибка при поиске аптек: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на поиск аптек.");
        }
        return FXCollections.observableArrayList();
    }
}
