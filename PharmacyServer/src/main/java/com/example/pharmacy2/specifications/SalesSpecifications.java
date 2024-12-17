package com.example.pharmacy2.specifications;

import com.example.pharmacy2.models.Sales;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class SalesSpecifications {
    public static Specification<Sales> isLikeSaleID(int saleId) {
        return (root, query, builder) ->  builder.equal(root.get("id"), saleId);//like(root.get("id"), "%" + medicineId + "%");
    }

    public static Specification<Sales> isLikeSaleDate(LocalDate date) {
        return (root, query, builder) ->  builder.equal(root.get("date"), date );
    }

    public static Specification<Sales> isLikeSaleMedID(int medId) {
        return (root, query, builder) ->  builder.equal(root.get("medicineId"), medId);//like(root.get("type").get("name"), "%" + typeName + "%");
    }

    public static Specification<Sales> isLikeSaleMedName(String medName) {
        return (root, query, builder) ->  builder.like(root.get("medicineName"), "%" +  medName + "%");//like(root.get("manufacturer").get("name"), "%" + manufacturerId + "%");
    }

    public static Specification<Sales> isLikePharmacy(String pharmacyAddress) {
        return (root, query, builder) ->  builder.like(root.get("pharmacy").get("address"), "%" +  pharmacyAddress + "%" );//like(root.get("productionDate"), "%" + productionDate + "%");
    }

}
