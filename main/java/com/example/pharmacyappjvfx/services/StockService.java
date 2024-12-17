package com.example.pharmacyappjvfx.services;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Arrays;

import com.example.pharmacyappjvfx.models.Medicine;
import com.example.pharmacyappjvfx.models.Pharmacy;
import com.example.pharmacyappjvfx.models.Stock;
import com.google.gson.Gson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class StockService {

    private static final StockService instance = new StockService();
    private final HttpClient httpClient = HttpClientManager.getInstance();
    private final String authHeader = AuthManager.getInstance().getBasicAuth();
    private final Gson gson = new Gson();

    private final Type listStockType = new TypeToken<List<Stock>>() {}.getType();

    private ObservableList<Stock> stocks;

    private StockService() {}

    public static StockService getInstance() {
        return instance;
    }

    // Метод для загрузки всех запасов
    public ObservableList<Stock> loadStocks() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/stocks/get_all"))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Stock> stockList = gson.fromJson(response.body(), listStockType);
                stocks = FXCollections.observableArrayList(stockList);
                return stocks;
            } else {
                System.err.println("Ошибка загрузки данных: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Метод для добавления нового запаса
    public void add(Stock stock) {
        try {
            String jsonStock = gson.toJson(stock);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/stocks/add"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonStock))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Запас успешно добавлен: " + response.body());
            } else {
                System.err.println("Ошибка при добавлении запаса: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на добавление запаса.");
        }
    }

    // Метод для обновления существующего запаса
    public void update(Stock stock) {
        try {
            String jsonStock = gson.toJson(stock);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/stocks/save"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonStock))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Запас успешно обновлён: " + response.body());
            } else {
                System.err.println("Ошибка при обновлении запаса: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на обновление запаса.");
        }
    }

    // Метод для удаления запаса
    public String delete(int medicineId, int pharmacyId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/stocks/delete/" + medicineId + "/" + pharmacyId))
                    .header("Authorization", authHeader)
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return ("Запас успешно удалён: " + response.body());
            } else {
                return ("Ошибка при удалении запаса: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на удаление запаса.");
        }
        return null;
    }

    // Метод для поиска запасов
    public ObservableList<Stock> search(Medicine medicineForId, Pharmacy pharmacyForId, String amountText) {
        try {
            StringBuilder uriBuilder = new StringBuilder("http://localhost:8080/stocks/search?");

            if (medicineForId != null) {
                uriBuilder.append("medicineId=").append(medicineForId.getId()).append("&");
            }
            if (pharmacyForId != null) {
                uriBuilder.append("pharmacyId=").append(pharmacyForId.getId()).append("&");
            }
            if (!amountText.isEmpty()) {
                uriBuilder.append("amount=").append(Integer.parseInt(amountText)).append("&");
            }
            String uri = uriBuilder.toString().replaceAll("&$", "");
            System.out.println(uri);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Stock> stockList = gson.fromJson(response.body(), listStockType);
                stocks = FXCollections.observableArrayList(stockList);
                return stocks;
            } else {
                System.err.println("Ошибка при поиске запасов: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на поиск запасов.");
        }
        return null;
    }
}
