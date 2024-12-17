package com.example.pharmacyappjvfx.models;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.Data;

import java.util.List;

@Data
public class Medicine {
    private int id;
    private String name;
    private TypeMed type;
    private Manufacturer manufacturer;
    private String productionDate;
    private String expirationDate;
    private int price;

    // Конструктор
    public Medicine() {}

    // Метод для парсинга JSON
    public static List<Medicine> parseMedicineList(String json) {
        Gson gson = new Gson();
        java.lang.reflect.Type listType = new TypeToken<List<Medicine>>() {}.getType();
        return gson.fromJson(json, listType);
    }
}
