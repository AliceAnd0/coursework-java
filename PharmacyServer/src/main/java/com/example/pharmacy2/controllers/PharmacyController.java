package com.example.pharmacy2.controllers;

import com.example.pharmacy2.models.Pharmacy;
import com.example.pharmacy2.models.TypeMed;
import com.example.pharmacy2.repositories.PharmacyRepository;
import com.example.pharmacy2.repositories.TypeMedRepository;
import com.example.pharmacy2.services.PharmacyService;
import com.example.pharmacy2.services.TypeMedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/pharmacies")
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    // Получить все аптеки
    @GetMapping("/get_all")
    public ResponseEntity<List<Pharmacy>> getAllPharmacies() {
        List<Pharmacy> pharmacies = pharmacyService.listAll(null);
        return ResponseEntity.ok(pharmacies);
    }

    // Поиск аптек по ключевому слову
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Pharmacy>> getFilteredPharmacies(@PathVariable String keyword) {
        // Декодируем ключевое слово и ищем аптеки по адресу или телефону
        List<Pharmacy> pharmacies = pharmacyService.listAll(URLDecoder.decode(keyword));
        return ResponseEntity.ok(pharmacies);
    }

    // Добавить аптеку
    @PostMapping("/add")
    public ResponseEntity<String> addPharmacy(@RequestBody Pharmacy pharmacy) {
        // Проверка на наличие данных для адреса и телефона
        if (pharmacy.getAddress() == null || pharmacy.getPhoneNumber() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Address and phone number are required");
        }

        pharmacyService.save(pharmacy);
        return ResponseEntity.ok("Pharmacy added successfully");
    }

    // Обновить информацию об аптеке
    @PutMapping("/update")
    public ResponseEntity<String> updatePharmacy(@RequestBody Pharmacy pharmacy) {
        // Проверяем, существует ли объект в базе данных
        if (pharmacy.getId() != 0) {
            pharmacyService.save(pharmacy);
            return ResponseEntity.ok("Pharmacy updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid pharmacy ID");
        }
    }

    // Удалить аптеку
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePharmacy(@PathVariable int id) {
        pharmacyService.delete(id);
        return ResponseEntity.ok("Pharmacy deleted successfully");
    }
}
