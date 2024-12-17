package com.example.pharmacyappjvfx.models;

import lombok.Data;

import java.time.LocalDate;
@Data
public class Sales {
    private int id;

    private Pharmacy pharmacy;
    private String date;

    private int medicineId;
    private String medicineName;
    private int amount;
    private int sum;
}
