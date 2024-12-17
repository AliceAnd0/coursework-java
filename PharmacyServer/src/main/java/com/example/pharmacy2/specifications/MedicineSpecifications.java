package com.example.pharmacy2.specifications;


import com.example.pharmacy2.models.Medicine2;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class MedicineSpecifications {
    /*public static Specification<Medicine2> isLikePharmacy(String pharmacyAddress) {
        return (root, query, builder) ->  builder.like(root.get("pharmacy"), "%" + pharmacyAddress + "%");
    }*/
    public static Specification<Medicine2> isLikeMedID(int medicineId) {
        return (root, query, builder) ->  builder.equal(root.get("id"), medicineId);//like(root.get("id"), "%" + medicineId + "%");
    }

    public static Specification<Medicine2> isLikeMedName(String medicineName) {
        return (root, query, builder) ->  builder.like(root.get("name"), "%" + medicineName + "%");
    }

    public static Specification<Medicine2> isLikeTypeName(int typeId) {
        return (root, query, builder) ->  builder.equal(root.get("type").get("id"), typeId);//like(root.get("type").get("name"), "%" + typeName + "%");
    }

    public static Specification<Medicine2> isLikeManufacturer(int manufacturerId) {
        return (root, query, builder) ->  builder.equal(root.get("manufacturer").get("id"), manufacturerId);//like(root.get("manufacturer").get("name"), "%" + manufacturerId + "%");
    }

    public static Specification<Medicine2> isLikeProductionDate(LocalDate productionDate) {
        return (root, query, builder) ->  builder.equal(root.get("productionDate"), productionDate );//like(root.get("productionDate"), "%" + productionDate + "%");
    }
    public static Specification<Medicine2> isLikeExpirationDate(LocalDate expirationDate) {
        return (root, query, builder) ->  builder.equal(root.get("expirationDate"), expirationDate);//like(root.get("expirationDate"), "%" + expirationDate + "%");
    }
    public static Specification<Medicine2> isLikePrice(int price) {
        return (root, query, builder) ->  builder.equal(root.get("price"), price);
                //like(root.get("price"), "%" + price + "%");
    }

}
