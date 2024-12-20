package com.example.pharmacy2.controllers;


import com.example.pharmacy2.models.Manufacturer;
import com.example.pharmacy2.models.Medicine2;
import com.example.pharmacy2.models.TypeMed;
import com.example.pharmacy2.repositories.ManufacturerRepository;
import com.example.pharmacy2.repositories.MedicineRepository;
import com.example.pharmacy2.services.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/manufacturers")
public class ManufacturerController {
    @Autowired
    private ManufacturerRepository repo;
    @Autowired
    private ManufacturerService manufacturerService;

    @GetMapping("/get_all")
    public ResponseEntity<List<Manufacturer>> getAllManufacturer() {
        List<Manufacturer> manufacturers = manufacturerService.listAll(null);
        return ResponseEntity.ok(manufacturers);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Manufacturer>> getFilteredManufacturer(@PathVariable String keyword) {
        List<Manufacturer> manufacturers = manufacturerService.listAll(URLDecoder.decode(keyword));
        return ResponseEntity.ok(manufacturers);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addManufacturer(@RequestBody Manufacturer manufacturer) {
        manufacturerService.save(manufacturer);
        return ResponseEntity.ok("Manufacturer added successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<String> saveManufacturer(@RequestBody Manufacturer manufacturer) {
        // Проверяем, существует ли объект в базе данных
        if (manufacturer.getId() != 0) {
            manufacturerService.save(manufacturer);
            return ResponseEntity.ok("Manufacturer updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid medicine ID");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteManufacturer (@PathVariable int id) {
        try{
            manufacturerService.delete(id);
            return ResponseEntity.ok("Manufacturer deleted successfully");
        } catch (DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot delete manufacturer: it is referenced in other tables.");
        }
    }


}