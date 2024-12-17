package com.example.pharmacyappjvfx.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;

import java.util.List;

@Data
public class Manufacturer {
    private int id;

    private String name;
    private String country;
    private String address;
    private String email;
}
