package com.example.pharmacy2.specifications;

import com.example.pharmacy2.models.Stock;
import org.springframework.data.jpa.domain.Specification;

public class StockSpecifications {

    // Фильтрация по идентификатору лекарства (medicineId)
    public static Specification<Stock> isLikeMedicineId(int medicineId) {
        return (root, query, builder) -> builder.equal(root.get("medicine").get("id"), medicineId);
    }

    // Фильтрация по идентификатору аптеки (pharmacyId)
    public static Specification<Stock> isLikePharmacyId(int pharmacyId) {
        return (root, query, builder) -> builder.equal(root.get("pharmacy").get("id"), pharmacyId);
    }

    // Фильтрация по количеству товара (amount)
    public static Specification<Stock> isLikeAmount(Integer amount) {
        return (root, query, builder) -> builder.equal(root.get("amount"), amount);
    }

}
