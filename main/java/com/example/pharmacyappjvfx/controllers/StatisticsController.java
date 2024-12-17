package com.example.pharmacyappjvfx.controllers;


import com.example.pharmacyappjvfx.models.Medicine;
import com.example.pharmacyappjvfx.services.AuthManager;
import com.example.pharmacyappjvfx.services.MedicineService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatisticsController {


    private final SceneSwitcher sceneSwitcher = SceneSwitcher.getInstance();
    private final AuthManager authManager = AuthManager.getInstance();
    private final MedicineService medicineService = MedicineService.getInstance();

    @FXML
    private Label usernameIndicator;
    @FXML
    private Label userRoleIndicator;
    @FXML
    private Label averagePriceLabel;
    @FXML
    private Label maxPriceLabel;
    @FXML
    private Label minPriceLabel;

    @FXML
    public void initialize() {

        usernameIndicator.setText("Пользователь: " + authManager.getUsername());
        userRoleIndicator.setText("Роль: " + authManager.getRole());

        Medicine maxMed = medicineService.findMaxPrice();
        Medicine minMed = medicineService.findMinPrice();

        averagePriceLabel.setText("Средняя цена: " + medicineService.findAveragePrice());
        maxPriceLabel.setText("Максимальная цена: " + maxMed.getPrice() + "(" + maxMed.getName() + ")");
        minPriceLabel.setText("Минимальная цена: " + minMed.getPrice() + "(" + minMed.getName() + ")");
    }

    @FXML
    public void switchToAuthor(){
        sceneSwitcher.switchTo("/views/author.fxml", false);
    }
    @FXML
    public void switchToMedicines(){
        sceneSwitcher.switchTo("/views/medicines.fxml", false);
    }
    @FXML
    public void switchToManufacturers(){
        sceneSwitcher.switchTo("/views/manufacturers.fxml", false);
    }
    @FXML
    public void switchToPharmacies(){
        sceneSwitcher.switchTo("/views/pharmacies.fxml", false);
    }
    @FXML
    public void switchToSales(){
        sceneSwitcher.switchTo("/views/sales.fxml", false);
    }
    @FXML
    public void switchToStock(){
        sceneSwitcher.switchTo("/views/stock.fxml", false);
    }
    @FXML
    public void switchToTypes(){
        sceneSwitcher.switchTo("/views/types.fxml", false);
    }
    @FXML
    public void switchToAuth(){
        sceneSwitcher.switchTo("/views/authentication.fxml", true);
    }
}