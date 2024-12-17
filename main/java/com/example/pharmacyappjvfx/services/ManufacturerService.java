package com.example.pharmacyappjvfx.services;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Arrays;

import com.example.pharmacyappjvfx.models.Manufacturer;
import com.example.pharmacyappjvfx.models.Medicine;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ManufacturerService {

    private static final ManufacturerService instance = new ManufacturerService();
    private final HttpClient httpClient = HttpClientManager.getInstance();
    private final String authHeader = AuthManager.getInstance().getBasicAuth();
    private final Gson gson = new Gson();

    private ManufacturerService() {}

    public static ManufacturerService getInstance() {
        return instance;
    }

    // Метод для загрузки всех производителей
    public List<Manufacturer> load(String authHeader) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/manufacturers/get_all"))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Преобразуем ответ в список производителей
                Gson gson = new Gson();
                Manufacturer[] manufacturers = gson.fromJson(response.body(), Manufacturer[].class);
                return Arrays.asList(manufacturers);
            } else {
                System.out.println("Ошибка загрузки производителей: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Метод для обновления информации о производителе
    public void update(Manufacturer manufacturer, String authHeader) {
        try {
            Gson gson = new Gson();
            String jsonManufacturer = gson.toJson(manufacturer);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/manufacturers/update"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonManufacturer))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Производитель успешно обновлен: " + response.body());
            } else {
                System.err.println("Ошибка при обновлении производителя: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на обновление производителя.");
        }
    }

    // Метод для удаления производителя
    public void delete(int manufacturerId, String authHeader) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/manufacturers/delete/" + manufacturerId))
                    .header("Authorization", authHeader)
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Производитель успешно удален: " + response.body());
            } else {
                System.err.println("Ошибка при удалении производителя: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на удаление производителя.");
        }
    }

    public void add(Manufacturer manufacturer, String authHeader) {
        try {
            String jsonManufacturer = gson.toJson(manufacturer);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/manufacturers/add"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonManufacturer))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Производитель успешно добавлено: " + response.body());
            } else {
                System.err.println("Ошибка при добавлении производителя: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на добавлении производителя.");
        }
    }

    public ObservableList<Manufacturer> search(String filter){
    try{
        String encodedFilter = URLEncoder.encode(filter, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()

                .uri(URI.create("http://localhost:8080/manufacturers/search/" + encodedFilter))
                .header("Authorization", authHeader)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Manufacturer[] manufacturers = gson.fromJson(response.body(), Manufacturer[].class);
            return FXCollections.observableArrayList(manufacturers);
        } else {
            System.err.println("Ошибка при поиске лекарства: " + response.statusCode());
        }
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Ошибка при отправке запроса на удаление лекарства.");
    }
        return null;
    }


}
