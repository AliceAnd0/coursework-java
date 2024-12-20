package com.example.pharmacy2.controllers;

import com.example.pharmacy2.models.Sales;
import com.example.pharmacy2.repositories.SalesRepository;
import com.example.pharmacy2.services.SalesService;
import com.example.pharmacy2.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    private SalesRepository salesRepository;
    @Autowired
    private SalesService salesService;

    // Получить все продажи
    @GetMapping("/get_all")
    public ResponseEntity<List<Sales>> getAllSales() {
        List<Sales> sales = salesService.listAll();
        return ResponseEntity.ok(sales);
    }

    // Поиск продаж по фильтрам
    @GetMapping("/search")
    public ResponseEntity<List<Sales>> searchSales(
            @RequestParam(required = false) Integer saleId,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Integer medicineId,
            @RequestParam(required = false) String medicineName,
            @RequestParam(required = false) String pharmacyAddress) {

        // Вызов метода фильтрации с переданными параметрами
        List<Sales> sales = salesService.filter(saleId, date, medicineId, medicineName, pharmacyAddress);
        return ResponseEntity.ok(sales);
    }

    // Добавить продажу
    @PostMapping("/add")
    public ResponseEntity<String> addSale(@RequestBody Sales sale) {
        // Проверяем, существует ли аптечный адрес
        if (sale.getPharmacy() == null || sale.getPharmacy().getAddress().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid pharmacy address");
        }
        sale.setMedicineName(URLDecoder.decode(sale.getMedicineName()));
        // Добавляем продажу в базу
        salesService.save(sale);
        return ResponseEntity.ok("Sale added successfully");
    }

    // Обновить продажу
    @PutMapping("/save")
    public ResponseEntity<String> saveSale(@RequestBody Sales sale) {
        // Проверяем, существует ли ID продажи
        if (sale.getId() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid sale ID");
        }

        // Проверяем, есть ли соответствующая запись в базе
        Sales existingSale = salesRepository.findById(sale.getId()).orElse(null);
        if (existingSale == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Sale not found");
        }

        // Обновляем информацию о продаже
        salesService.save(sale);
        return ResponseEntity.ok("Sale updated successfully");
    }

    // Удалить продажу
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSale(@PathVariable int id) {
        // Проверяем, существует ли запись для удаления
        Sales existingSale = salesRepository.findById(id).orElse(null);
        if (existingSale == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Sale not found");
        }

        salesService.delete(id);
        return ResponseEntity.ok("Sale deleted successfully");
    }
}
