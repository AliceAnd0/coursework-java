package com.example.pharmacy2.models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "stock")
public class Stock {

    @EmbeddedId
    private StockPK pk;

    @ManyToOne
    @MapsId("medicineId")
    @JoinColumn(name = "medicine_id")
    private Medicine2 medicine;

    @ManyToOne
    @MapsId("pharmacyId")
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    private Integer amount;

}