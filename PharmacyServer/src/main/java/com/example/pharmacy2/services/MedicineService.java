package com.example.pharmacy2.services;

import com.example.pharmacy2.models.Medicine2;
import com.example.pharmacy2.repositories.MedicineRepository;
import com.example.pharmacy2.specifications.MedicineSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class MedicineService {
    @Autowired
    private MedicineRepository repo;

    public List<Medicine2> listAll() {
        /*if (keyword != null) {
            return repo.search(keyword);
        }*/
        return repo.findAll();
    }

    public List<Medicine2> filter(Integer medicineId, String medicineName, Integer typeId,
                                  Integer manufacturerId, LocalDate productionDate,
                                  LocalDate expirationDate, Integer price) {
        // Начинаем с пустой спецификации
        Specification<Medicine2> spec = Specification.where(null);

        // Проверяем каждый параметр, если он не null, добавляем соответствующую спецификацию
        if (medicineId != null) {
            spec = spec.and(MedicineSpecifications.isLikeMedID(medicineId));
        }

        if (medicineName != null && !medicineName.isEmpty()) {
            spec = spec.and(MedicineSpecifications.isLikeMedName(medicineName));
        }

        if (typeId != null) {
            spec = spec.and(MedicineSpecifications.isLikeTypeName(typeId));
        }

        if (manufacturerId != null) {
            spec = spec.and(MedicineSpecifications.isLikeManufacturer(manufacturerId));
        }

        if (productionDate != null) {
            spec = spec.and(MedicineSpecifications.isLikeProductionDate(productionDate));
        }

        if (expirationDate != null) {
            spec = spec.and(MedicineSpecifications.isLikeExpirationDate(expirationDate));
        }

        if (price != null) {
            spec = spec.and(MedicineSpecifications.isLikePrice(price));
        }

        // Возвращаем все результаты, которые соответствуют спецификации
        return repo.findAll(spec);
    }
    //keyword - название лекарства
    //@PreAuthorize("hasRole('ADMIN')")
    public void save(Medicine2 medicine) {
        repo.save(medicine);
    }

    public Medicine2 get(Integer id) {
        return repo.findById(id).get();
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
