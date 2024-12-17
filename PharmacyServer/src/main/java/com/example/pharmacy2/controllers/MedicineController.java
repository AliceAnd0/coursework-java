package com.example.pharmacy2.controllers;

import com.example.pharmacy2.models.Manufacturer;
import com.example.pharmacy2.models.Medicine2;
import com.example.pharmacy2.models.TypeMed;
import com.example.pharmacy2.repositories.ManufacturerRepository;
import com.example.pharmacy2.repositories.MedicineRepository;
import com.example.pharmacy2.repositories.TypeMedRepository;
import com.example.pharmacy2.services.MedicineService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/medicines")
public class MedicineController {
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private TypeMedRepository typeRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private MedicineService medicineService;

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get_all")
    public ResponseEntity<List<Medicine2>> getAllMedicines () {
        List<Medicine2> medicines = medicineService.listAll();
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Medicine2>> searchMedicines (
            @RequestParam(required = false) Integer medId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer typeId,
            @RequestParam(required = false) Integer manufacturerId,
            @RequestParam(required = false) LocalDate productionDate,
            @RequestParam(required = false) LocalDate expirationDate,
            @RequestParam(required = false) Integer price) {

        // Вызов метода фильтрации с переданными параметрами
        List<Medicine2> medicines = medicineService.filter(medId, name, typeId, manufacturerId, productionDate, expirationDate, price);
        //System.out.println(productionDate.toString());
        return ResponseEntity.ok(medicines);
    }

    @PutMapping("/save")
    public ResponseEntity<String> saveMedicine(@RequestBody Medicine2 medicine) {
        // Проверяем, существует ли объект в базе данных
        if (medicine.getId() != 0) {
            // Проверяем, существует ли нужный тип лекарства
            TypeMed type = typeRepository.findById(medicine.getType().getId()).orElse(null);
            if (type == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid type ID");
            }

            // Проверяем, существует ли нужный производитель
            Manufacturer manufacturer = manufacturerRepository.findById(medicine.getManufacturer().getId()).orElse(null);
            if (manufacturer == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid manufacturer ID");
            }

            // Устанавливаем правильные связанные сущности
            medicine.setType(type);
            medicine.setManufacturer(manufacturer);

            // Сохраняем обновленное лекарство
            medicineService.save(medicine);
            System.out.println(medicine);
            return ResponseEntity.ok("Medicine updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid medicine ID");
        }
    }


    @PostMapping("/add")
    public ResponseEntity<String> addMedicine(@RequestBody Medicine2 medicine) {
            // Проверяем, существует ли нужный тип лекарства
            TypeMed type = typeRepository.findById(medicine.getType().getId()).orElse(null);
            if (type == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid type ID");
            }

            // Проверяем, существует ли нужный производитель
            Manufacturer manufacturer = manufacturerRepository.findById(medicine.getManufacturer().getId()).orElse(null);
            if (manufacturer == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid manufacturer ID");
            }

            // Устанавливаем правильные связанные сущности
            medicine.setType(type);
            medicine.setManufacturer(manufacturer);

            // Сохраняем обновленное лекарство
            medicineService.save(medicine);
            return ResponseEntity.ok("Medicine added successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMedicine (@PathVariable Integer id) {
        medicineService.delete(id);
        return ResponseEntity.ok("Medicine Deleted");
    }

}
