package com.example.pharmacyappjvfx.services;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import com.example.pharmacyappjvfx.models.Manufacturer;
import com.example.pharmacyappjvfx.models.Medicine;

import com.example.pharmacyappjvfx.models.TypeMed;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MedicineService {

    private static final MedicineService instance = new MedicineService();
    private final HttpClient httpClient = HttpClientManager.getInstance();
    private final String authHeader = AuthManager.getInstance().getBasicAuth();

    private final Type listMedType = new TypeToken<List<Medicine>>() {}.getType();

    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Gson gson = new Gson();

    private ObservableList<Medicine> medicines;

    private MedicineService() {}

    public static MedicineService getInstance() {
        return instance;
    }

    // Метод для загрузки всех лекарств
    public ObservableList<Medicine> loadMedicines() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/medicines/get_all"))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Medicine> medicineList = gson.fromJson(response.body(), listMedType);

                medicineList.forEach(medicine -> {
                    medicine.setProductionDate(reformatDate(medicine.getProductionDate()));
                    medicine.setExpirationDate(reformatDate(medicine.getExpirationDate()));
                });
                medicines = FXCollections.observableArrayList(medicineList);
                return medicines;
            } else {
                System.out.println("Ошибка загрузки данных: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void add(Medicine medicine) {
        try {
            medicine.setName(URLEncoder.encode(medicine.getName(), StandardCharsets.UTF_8));
            String jsonMedicine = gson.toJson(medicine);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/medicines/add"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonMedicine))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Лекарство успешно добавлено: " + response.body());
            } else {
                System.err.println("Ошибка при добавлении лекарства: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на добавлении лекарства.");
        }
    }

    // Метод для обновления лекарства
    public void updateMedicine(Medicine medicine) {
        try {
            medicine.setName(URLEncoder.encode(medicine.getName(), StandardCharsets.UTF_8));
            String jsonMedicine = gson.toJson(medicine);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/medicines/save"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonMedicine))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Лекарство успешно обновлено: " + response.body());
            } else {
                System.err.println("Ошибка при обновлении лекарства: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на обновление лекарства.");
        }
    }

    // Метод для удаления лекарства
    public String deleteMedicine(int medicineId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/medicines/delete/" + medicineId))
                    .header("Authorization", authHeader)
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return ("Лекарство успешно удалено: " + response.body());
            } else {
                return ("Ошибка при удалении лекарства: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на удаление лекарства.");
        }
        return null;
    }

    public List<Medicine> search(String medId, String name, TypeMed medType, Manufacturer manufacturer, LocalDate productionDate,
                                 LocalDate expirationDate, String price) {
        try {
            StringBuilder uriBuilder = new StringBuilder("http://localhost:8080/medicines/search?");

            if (!medId.isEmpty()) {
                uriBuilder.append("medId=").append(Integer.valueOf(medId)).append("&");
            }
            if (!name.isEmpty()) {
                String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
                uriBuilder.append("name=").append(encodedName).append("&");
            }
            if (medType != null) {
                uriBuilder.append("typeId=").append(medType.getId()).append("&");
            }
            if (manufacturer != null) {
                uriBuilder.append("manufacturerId=").append(manufacturer.getId()).append("&");
            }
            if (productionDate != null) {
                uriBuilder.append("productionDate=").append(productionDate).append("&");
            }
            if (expirationDate != null) {
                uriBuilder.append("expirationDate=").append(expirationDate).append("&");
            }
            if (!price.isEmpty()) {
                uriBuilder.append("price=").append(Integer.valueOf(price)).append("&");
            }
            // Убираем лишний "&" в конце, если он есть
            String uri = uriBuilder.toString().replaceAll("&$", "");
            System.out.println(uri);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Medicine> medicineList = gson.fromJson(response.body(), listMedType);

                medicineList.forEach(medicine -> {
                    medicine.setProductionDate(reformatDate(medicine.getProductionDate()));
                    medicine.setExpirationDate(reformatDate(medicine.getExpirationDate()));
                });
                medicines = FXCollections.observableArrayList(medicineList);
                return medicines;
            } else {
                System.err.println("Ошибка при поиске лекарства: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке запроса на удаление лекарства.");
        }
        return null;
    }

    public Medicine findMaxPrice() {
        if (medicines.isEmpty()) {
            return null;
        }

        // Поиск максимальной цены
        return medicines.stream()
                .max(Comparator.comparingInt(Medicine::getPrice))
                .orElse(null);
    }

    public Medicine findMinPrice() {
        if (medicines.isEmpty()) {
            return null;
        }

        // Поиск минимальной цены
        return medicines.stream()
                .min(Comparator.comparingInt(Medicine::getPrice))
                .orElse(null);
    }

    public double findAveragePrice() {
        if (medicines.isEmpty()) {
            return 0;
        }

        // Расчет средней цены
        return medicines.stream()
                .mapToInt(Medicine::getPrice)
                .average()
                .orElse(0.0);
    }

    private String reformatDate(String inputDate) {
        System.out.println(inputDate + INPUT_FORMATTER);
        LocalDate date = LocalDate.parse(inputDate, INPUT_FORMATTER);
        return date.format(OUTPUT_FORMATTER);
    }

    public Medicine findMedicineById(int id) {
        for (Medicine medicine : medicines) {
            if (medicine.getId() == id) {
                return medicine;  // Возвращаем объект, если найдено совпадение
            }
        }
        return null;  // Если объект не найден
    }

}
