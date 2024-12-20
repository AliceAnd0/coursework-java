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
import com.example.pharmacyappjvfx.models.Sales;
import com.google.gson.Gson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SalesService {

    private static final SalesService instance = new SalesService();
    private final HttpClient httpClient = HttpClientManager.getInstance();
    private final String authHeader = AuthManager.getInstance().getBasicAuth();

    private final Type listSalesType = new TypeToken<List<Sales>>() {}.getType();

    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Gson gson = new Gson();

    private ObservableList<Sales> sales;

    private SalesService() {}

    public static SalesService getInstance() {
        return instance;
    }

    // Метод для загрузки всех продаж
    public ObservableList<Sales> loadSales() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/sales/get_all"))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Sales> salesList = gson.fromJson(response.body(), listSalesType);

                salesList.forEach(sale -> sale.setDate(reformatDate(sale.getDate())));
                sales = FXCollections.observableArrayList(salesList);
                return sales;
            } else {
                System.out.println("Ошибка загрузки данных: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Метод для добавления новой продажи
    public void add(Sales sale) {
        try {
            sale.setMedicineName(URLEncoder.encode(sale.getMedicineName(), StandardCharsets.UTF_8));
            String jsonSale = gson.toJson(sale);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/sales/add"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonSale))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Продажа успешно добавлена: " + response.body());
            } else {
                System.err.println("Ошибка при добавлении продажи: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на добавление продажи.");
        }
    }

    // Метод для обновления продажи
    public void update(Sales sale) {
        try {
            sale.setMedicineName(URLEncoder.encode(sale.getMedicineName(), StandardCharsets.UTF_8));
            String jsonSale = gson.toJson(sale);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/sales/save"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonSale))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Продажа успешно обновлена: " + response.body());
            } else {
                System.err.println("Ошибка при обновлении продажи: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на обновление продажи.");
        }
    }

    // Метод для удаления продажи
    public Integer delete(int saleId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/sales/delete/" + saleId))
                    .header("Authorization", authHeader)
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return 200;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на удаление продажи.");
        }
        return null;
    }

    // Метод для поиска продаж
    public ObservableList<Sales> search(String saleId, LocalDate date, Medicine medicineForId, Medicine medicineForName, Pharmacy pharmacyForAddress) {
        try {
            StringBuilder uriBuilder = new StringBuilder("http://localhost:8080/sales/search?");

            if (!saleId.isEmpty()) {
                uriBuilder.append("saleId=").append(Integer.valueOf(saleId)).append("&");
            }
            if (date != null) {
                uriBuilder.append("date=").append(date).append("&");
            }
            if (medicineForId != null) {
                uriBuilder.append("medicineId=").append(medicineForId.getId()).append("&");
            }
            if (medicineForName != null) {
                String encodedName = URLEncoder.encode(medicineForName.getName(), StandardCharsets.UTF_8);
                uriBuilder.append("medicineName=").append(encodedName).append("&");
            }
            if (pharmacyForAddress != null) {
                String encodedPharmacyName = URLEncoder.encode(pharmacyForAddress.getAddress(), StandardCharsets.UTF_8);
                uriBuilder.append("pharmacyName=").append(encodedPharmacyName).append("&");
            }
            // Убираем лишний "&" в конце, если он есть
            String uri = uriBuilder.toString().replaceAll("&$", "");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Sales> salesList = gson.fromJson(response.body(), listSalesType);

                salesList.forEach(sale -> sale.setDate(reformatDate(sale.getDate())));
                sales = FXCollections.observableArrayList(salesList);
                return sales;
            } else {
                System.err.println("Ошибка при поиске продажи: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на поиск продажи.");
        }
        return null;
    }

    // Метод для форматирования даты
    private String reformatDate(String inputDate) {
        LocalDate date = LocalDate.parse(inputDate, INPUT_FORMATTER);
        return date.format(OUTPUT_FORMATTER);
    }

}

