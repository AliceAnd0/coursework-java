package com.example.pharmacy2.services;

import com.example.pharmacy2.models.Sales;
import com.example.pharmacy2.repositories.SalesRepository;
import com.example.pharmacy2.specifications.SalesSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SalesService {

    @Autowired
    private SalesRepository repoSales;

    public List<Sales> listAll() {
        return repoSales.findAll();
    }

    public List<Sales> filter(Integer saleId, LocalDate date, Integer medicineId,
                              String medicineName, String pharmacyAddress) {
        // Начинаем с пустой спецификации
        Specification<Sales> spec = Specification.where(null);

        // Проверяем каждый параметр, если он не null, добавляем соответствующую спецификацию
        if (saleId != null) {
            spec = spec.and(SalesSpecifications.isLikeSaleID(saleId));
        }

        if (date != null) {
            spec = spec.and(SalesSpecifications.isLikeSaleDate(date));
        }

        if (medicineId != null) {
            spec = spec.and(SalesSpecifications.isLikeSaleMedID(medicineId));
        }

        if (medicineName != null && !medicineName.isEmpty()) {
            spec = spec.and(SalesSpecifications.isLikeSaleMedName(medicineName));
        }

        if (pharmacyAddress != null && !pharmacyAddress.isEmpty()) {
            spec = spec.and(SalesSpecifications.isLikePharmacy(pharmacyAddress));
        }

        // Возвращаем все результаты, которые соответствуют спецификации
        return repoSales.findAll(spec);
    }
    public void save(Sales sales) {
        repoSales.save(sales);
    }

    public Sales get(int id) {
        return repoSales.findById(id).get();
    }

    public void delete(int id) {
        repoSales.deleteById(id);
    }
}
