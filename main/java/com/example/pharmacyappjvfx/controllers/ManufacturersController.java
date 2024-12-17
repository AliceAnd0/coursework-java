package com.example.pharmacyappjvfx.controllers;

import com.example.pharmacyappjvfx.models.Manufacturer;
import com.example.pharmacyappjvfx.models.Medicine;
import com.example.pharmacyappjvfx.services.AuthManager;
import com.example.pharmacyappjvfx.services.ManufacturerService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.List;

public class ManufacturersController {
    @FXML
    private Label usernameIndicator;
    @FXML
    private Label userRoleIndicator;

    @FXML
    private TextField searchAny;

    @FXML
    private TableView<Manufacturer> manufacturerTable;
    @FXML
    private TableColumn<Manufacturer, Integer> idColumn;
    @FXML
    private TableColumn<Manufacturer, String> nameColumn;
    @FXML
    private TableColumn<Manufacturer, String> countryColumn;
    @FXML
    private TableColumn<Manufacturer, String> addressColumn;
    @FXML
    private TableColumn<Manufacturer, String> emailColumn;

    @FXML
    private VBox vBoxAdminForm;
    @FXML
    private TextField nameField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField emailField;

    @FXML
    private Label actionFormLabel;
    @FXML
    private Label actionGuide;
    @FXML
    private Label rowCount;

    private boolean isEditButtonPressed = false;
    private boolean isDeleteButtonPressed = false;

    private final ManufacturerService manufacturerService = ManufacturerService.getInstance();
    private final SceneSwitcher sceneSwitcher = SceneSwitcher.getInstance();
    private final AuthManager authManager = AuthManager.getInstance();

    private Manufacturer selectedManufacturer;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        setAccessToAdminForm();
        loadManufacturers();
        setTableSelectionListener();
        updateRowCount();
        usernameIndicator.setText("Пользователь: " + authManager.getUsername());
        userRoleIndicator.setText("Роль: " + authManager.getRole());
    }

    private void setTableSelectionListener() {
        manufacturerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedManufacturer = newValue;
            if (isDeleteButtonPressed && newValue != null) {
                showDeleteConfirmation(newValue);
            } else if (isEditButtonPressed && newValue != null) {
                fillFormForEdit(newValue);
            }
        });
    }

    public void loadManufacturers() {
        List<Manufacturer> manufacturers = manufacturerService.load(AuthManager.getInstance().getBasicAuth());
        if (manufacturers != null) {
            ObservableList<Manufacturer> observableList = FXCollections.observableArrayList(manufacturers);
            manufacturerTable.setItems(observableList);
        }
    }

    private void fillFormForEdit(Manufacturer manufacturer) {
        actionGuide.setText("Отредактируйте данные в форме");
        actionFormLabel.setText("Редактирование");

        nameField.setText(manufacturer.getName());
        countryField.setText(manufacturer.getCountry());
        addressField.setText(manufacturer.getAddress());
        emailField.setText(manufacturer.getEmail());
    }

    @FXML
    public void addManufacturer() {

        Manufacturer newManufacturer = new Manufacturer();
        newManufacturer.setName(nameField.getText());
        newManufacturer.setCountry(countryField.getText());
        newManufacturer.setAddress(addressField.getText());
        newManufacturer.setEmail(emailField.getText());

        try {
            manufacturerService.add(newManufacturer, AuthManager.getInstance().getBasicAuth());
            loadManufacturers();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка добавления. Попробуйте снова.");
        }
    }

    /*@FXML
    public void editManufacturer() {
        fillFormForEdit(selectedManufacturer);
    }*/

    @FXML
    public void saveManufacturer() {
        try {
            checkForm();
            if (selectedManufacturer != null && isEditButtonPressed) {
                updateManufacturer();
                isEditButtonPressed = false;
                clearForm();
                loadManufacturers();
            } else {
                addManufacturer();
                clearForm();
                loadManufacturers();
            }
        } catch (Exception e) {
            showFormWarning(e.getMessage());
        }
    }

    public void updateManufacturer() {
        Manufacturer updatedManufacturer = new Manufacturer();
        updatedManufacturer.setId(selectedManufacturer.getId());
        updatedManufacturer.setName(nameField.getText());
        updatedManufacturer.setCountry(countryField.getText());
        updatedManufacturer.setAddress(addressField.getText());
        updatedManufacturer.setEmail(emailField.getText());

        try {
            manufacturerService.update(updatedManufacturer, AuthManager.getInstance().getBasicAuth());
            loadManufacturers();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка обновления. Попробуйте снова.");
        }
    }

    @FXML
    private void handleAddAction() {
        isEditButtonPressed = false;
        isDeleteButtonPressed = false;
        actionGuide.setText("Заполните данные в форму");
    }

    @FXML
    private void handleEditAction() {
        isDeleteButtonPressed = false;
        isEditButtonPressed = true;
        actionGuide.setText("Выберите запись для редактирования");
    }

    @FXML
    private void handleDeleteAction() {
        isEditButtonPressed = false;
        isDeleteButtonPressed = true;
        actionGuide.setText("Выберите запись для удаления");
    }

    @FXML
    private void clearForm() {
        actionFormLabel.setText("Форма для добавления и редактирования");
        nameField.clear();
        countryField.clear();
        addressField.clear();
        emailField.clear();
        isDeleteButtonPressed = false;
        actionGuide.setText("Выберите действие");
    }

    private void checkForm() throws Exception {
        if (nameField.getText().isEmpty()) {
            throw new Exception("Введите наименование производителя");
        }
        if (countryField.getText().isEmpty()) {
            throw new Exception("Введите страну производителя");
        }
        if (addressField.getText().isEmpty()) {
            throw new Exception("Введите адрес производителя");
        }
        if (emailField.getText().isEmpty()) {
            throw new Exception("Введите email производителя");
        }
        if(!emailField.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new Exception("Email должен соответствовать виду '_@_._' ");
        }
    }

    private void showFormWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText("Все поля должны быть заполнены");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showDeleteConfirmation(Manufacturer manufacturer) {
        isDeleteButtonPressed = false;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Вы уверены, что хотите удалить запись?");
        alert.setContentText("Наименование: " + manufacturer.getName());

        ButtonType okButton = new ButtonType("ОК", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, cancelButton);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == okButton) {
                manufacturerService.delete(manufacturer.getId(), AuthManager.getInstance().getBasicAuth());
                loadManufacturers();
                actionFormLabel.setText("Запись удалена");
            } else {
                actionFormLabel.setText("Удаление отменено");
            }
            actionGuide.setText("Выберите действие");
        });
    }

    @FXML
    public void clearFilter(){
        searchAny.clear();
        loadManufacturers();
        updateRowCount();
    }

    @FXML
    public void filterSearch(){
        ObservableList<Manufacturer> filteredManufacturers= manufacturerService.search(searchAny.getText());
        if (filteredManufacturers != null) {
            manufacturerTable.setItems(filteredManufacturers);
            updateRowCount();
        }
    }

    private void updateRowCount() {
        int rowNumber = manufacturerTable.getItems().size();
        rowCount.setText("Количество производителей: " + rowNumber);
    }

    @FXML
    public void switchToMedicines() {
        sceneSwitcher.switchTo("/views/medicines.fxml", false);
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
    public void switchToAuthor(){
        sceneSwitcher.switchTo("/views/author.fxml", false);
    }
    @FXML
    public void switchToStatistics(){
        sceneSwitcher.switchTo("/views/statistics.fxml", false);
    }
    @FXML
    public void switchToAuth(){
        sceneSwitcher.switchTo("/views/authentication.fxml", true);
    }

    private void setAccessToAdminForm(){
        if ("ROLE_ADMIN".equals(authManager.getRole())) {
            vBoxAdminForm.setVisible(true); // Учитываем VBox в макете
        } else {
            vBoxAdminForm.setVisible(false); // Убираем VBox из макета
        }
    }

}
