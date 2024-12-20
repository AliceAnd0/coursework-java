package com.example.pharmacy2.controllers;

import com.example.pharmacy2.models.Medicine2;
import com.example.pharmacy2.models.Pharmacy;
import com.example.pharmacy2.models.Stock;
import com.example.pharmacy2.repositories.MedicineRepository;
import com.example.pharmacy2.repositories.PharmacyRepository;
import com.example.pharmacy2.repositories.StockRepository;
import com.example.pharmacy2.services.PharmacyService;
import com.example.pharmacy2.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    // Получить все записи о запасах
    @GetMapping("/get_all")
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = stockService.listAll();
        return ResponseEntity.ok(stocks);
    }

    // Поиск запасов по ключевым параметрам
    @GetMapping("/search")
    public ResponseEntity<List<Stock>> searchStocks(
            @RequestParam(required = false) Integer medicineId,
            @RequestParam(required = false) Integer pharmacyId,
            @RequestParam(required = false) Integer amount) {

        List<Stock> stocks = stockService.filter(medicineId, pharmacyId, amount);
        return ResponseEntity.ok(stocks);
    }

    // Добавить запись о запасах
    @PostMapping("/add")
    public ResponseEntity<String> addStock(@RequestBody Stock stock) {
        if (stock.getMedicine() == null || stock.getPharmacy() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Medicine and Pharmacy must be provided");
        }

        // Проверяем, существует ли нужное лекарство
        Medicine2 medicine = medicineRepository.findById(stock.getMedicine().getId()).orElse(null);
        if (medicine == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid Medicine ID");
        }

        // Проверяем, существует ли нужная аптека
        Pharmacy pharmacy = pharmacyRepository.findById(stock.getPharmacy().getId()).orElse(null);
        if (pharmacy == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid Pharmacy ID");
        }

        // Создаем составной ключ для записи о запасах
        stockService.save(stock, medicine.getId(), pharmacy.getId());

        return ResponseEntity.ok("Stock added successfully");
    }

    // Обновить информацию о запасах
    @PutMapping("/update")
    public ResponseEntity<String> updateStock(@RequestBody Stock stock) {
        if (stock.getPk() == null || stock.getPk().getMedicineId() == 0 || stock.getPk().getPharmacyId() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid stock ID");
        }

        // Проверяем, существует ли запись с таким составным ключом
        Stock existingStock = stockService.get(stock.getPk().getMedicineId(), stock.getPk().getPharmacyId());
        if (existingStock == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Stock record not found");
        }

        stockService.saveEdittedStock(stock);
        return ResponseEntity.ok("Stock updated successfully");
    }

    // Удалить запись о запасах
    @DeleteMapping("/delete/{medicineId}/{pharmacyId}")
    public ResponseEntity<String> deleteStock(@PathVariable int medicineId, @PathVariable int pharmacyId) {
        // Проверяем, существует ли запись с таким составным ключом
        Stock existingStock = stockService.get(medicineId, pharmacyId);
        if (existingStock == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Stock record not found");
        }

        stockService.delete(medicineId, pharmacyId);
        return ResponseEntity.ok("Stock deleted successfully");
    }
}
