package com.example.pharmacyappjvfx.controllers;

import com.example.pharmacyappjvfx.models.Medicine;
import com.example.pharmacyappjvfx.services.AuthManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AuthorController {

    private final SceneSwitcher sceneSwitcher = SceneSwitcher.getInstance();
    private final AuthManager authManager = AuthManager.getInstance();

    @FXML
    public void initialize() {

        usernameIndicator.setText("Пользователь: " + authManager.getUsername());
        userRoleIndicator.setText("Роль: " + authManager.getRole());
    }

    @FXML
    private Label usernameIndicator;
    @FXML
    private Label userRoleIndicator;


    @FXML
    public void switchToStatistics(){
        sceneSwitcher.switchTo("/views/statistics.fxml", false);
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
