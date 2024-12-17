package com.example.pharmacyappjvfx.services;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import com.example.pharmacyappjvfx.models.TypeMed;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TypeService {

    private static final TypeService instance = new TypeService();
    private final HttpClient httpClient = HttpClientManager.getInstance();
    private final String authHeader = AuthManager.getInstance().getBasicAuth();
    private final Gson gson = new Gson();

    private TypeService() {}

    public static TypeService getInstance() {
        return instance;
    }

    // Метод для загрузки всех типов медикаментов
    public ObservableList<TypeMed> load() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/types/get_all"))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                TypeMed[] typeMeds = gson.fromJson(response.body(), TypeMed[].class);
                return FXCollections.observableArrayList(typeMeds);
            } else {
                System.err.println("Ошибка загрузки типов медикаментов: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на загрузку типов медикаментов.");
        }
        return FXCollections.observableArrayList();
    }

    // Метод для обновления данных о типе медикамента
    public void update(TypeMed typeMed) {
        try {
            String jsonTypeMed = gson.toJson(typeMed);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/types/update"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonTypeMed))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Информация о типе медикамента успешно обновлена: " + response.body());
            } else {
                System.err.println("Ошибка при обновлении информации о типе медикамента: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на обновление информации о типе медикамента.");
        }
    }

    // Метод для удаления типа медикамента
    public void delete(int typeMedId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/types/delete/" + typeMedId))
                    .header("Authorization", authHeader)
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Тип медикамента успешно удален: " + response.body());
            } else {
                System.err.println("Ошибка при удалении типа медикамента: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на удаление типа медикамента.");
        }
    }

    // Метод для добавления типа медикамента
    public void add(TypeMed typeMed) {
        try {
            String jsonTypeMed = gson.toJson(typeMed);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/types/add"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonTypeMed))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Тип медикамента успешно добавлен: " + response.body());
            } else {
                System.err.println("Ошибка при добавлении типа медикамента: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на добавление типа медикамента.");
        }
    }

    // Метод для поиска типов медикаментов
    public ObservableList<TypeMed> search(String filter) {
        try {
            String encodedFilter = URLEncoder.encode(filter, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/types/search/" + encodedFilter))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                TypeMed[] typeMeds = gson.fromJson(response.body(), TypeMed[].class);
                return FXCollections.observableArrayList(typeMeds);
            } else {
                System.err.println("Ошибка при поиске типов медикаментов: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на поиск типов медикаментов.");
        }
        return FXCollections.observableArrayList();
    }
}