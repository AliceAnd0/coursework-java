package com.example.pharmacy2.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;
    //private String pharmacyAddress;
    private LocalDate date;
    //@OneToOne
    //private Medicine2 medicine;
    private int medicineId;
    private String medicineName;
    private int amount;
    private int sum;

    public Sales() {}

}