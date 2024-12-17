package com.example.pharmacyappjvfx.models;

import lombok.Data;

@Data
public class Stock {
    private StockPK pk;
    private Medicine medicine;
    private Pharmacy pharmacy;
    private Integer amount;
}
