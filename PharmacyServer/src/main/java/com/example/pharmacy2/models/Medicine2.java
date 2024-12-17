package com.example.pharmacy2.models;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "medicine2")
public class Medicine2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private TypeMed type;
    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;
    @Column(name = "expiration_date", columnDefinition = "DATE")
    private LocalDate expirationDate;

    @Column(name = "production_date",columnDefinition = "DATE")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate productionDate;
    private BigDecimal price;

    //protected Medicine2() {}
    public Medicine2() {}
}