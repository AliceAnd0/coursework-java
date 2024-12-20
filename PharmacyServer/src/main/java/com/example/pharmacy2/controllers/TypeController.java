package com.example.pharmacy2.controllers;

import com.example.pharmacy2.models.Medicine2;
import com.example.pharmacy2.models.TypeMed;
import com.example.pharmacy2.repositories.MedicineRepository;
import com.example.pharmacy2.repositories.PharmacyRepository;
import com.example.pharmacy2.repositories.TypeMedRepository;
import com.example.pharmacy2.services.TypeMedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/types")
public class TypeController {

    @Autowired
    private TypeMedService typeMedService;

    // Получить все типы
    @GetMapping("/get_all")
    public ResponseEntity<List<TypeMed>> getAllTypes() {
        List<TypeMed> types = typeMedService.listAll(null);
        return ResponseEntity.ok(types);
    }

    // Поиск типов по ключевому слову
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<TypeMed>> getFilteredTypes(@PathVariable String keyword) {
        // Декодируем ключевое слово и ищем типы по имени
        List<TypeMed> types = typeMedService.listAll(URLDecoder.decode(keyword));
        return ResponseEntity.ok(types);
    }

    // Добавить тип
    @PostMapping("/add")
    public ResponseEntity<String> addType(@RequestBody TypeMed typeMed) {
        // Проверка на наличие данных для имени типа
        if (typeMed.getName() == null || typeMed.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Type name is required");
        }

        typeMedService.save(typeMed);
        return ResponseEntity.ok("Type added successfully");
    }

    // Обновить информацию о типе
    @PutMapping("/update")
    public ResponseEntity<String> updateType(@RequestBody TypeMed typeMed) {
        // Проверяем, существует ли объект в базе данных
        if (typeMed.getId() != 0) {
            typeMedService.save(typeMed);
            return ResponseEntity.ok("Type updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid type ID");
        }
    }

    // Удалить тип
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteType(@PathVariable int id) {
        try{
            typeMedService.delete(id);
            return ResponseEntity.ok("Type deleted successfully");
        } catch (DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot delete type: it is referenced in other tables.");
        }
    }
}
