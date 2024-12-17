package com.example.pharmacy2.models;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class StockPK implements Serializable {
    private int medicineId;
    private int pharmacyId;

    // Пустой конструктор (обязателен для JPA)
    public StockPK() {}

    // Конструктор с параметрами
    public StockPK(int id1, int id2) {
        this.medicineId = id1;
        this.pharmacyId = id2;
    }
}