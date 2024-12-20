package com.example.pharmacyappjvfx.controllers;

import com.example.pharmacyappjvfx.models.Manufacturer;
import com.example.pharmacyappjvfx.models.Medicine;
import com.example.pharmacyappjvfx.models.TypeMed;
import com.example.pharmacyappjvfx.services.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MedicinesController {

    @FXML
    private TableView<Medicine> medicineTable;
    @FXML
    private TableColumn<Medicine, Integer> idColumn;
    @FXML
    private TableColumn<Medicine, String> nameColumn;
    @FXML
    private TableColumn<Medicine, String> typeColumn;
    @FXML
    private TableColumn<Medicine, String> manufacturerColumn;
    @FXML
    private TableColumn<Medicine, String> productionDateColumn;
    @FXML
    private TableColumn<Medicine, String> expirationDateColumn;
    @FXML
    private TableColumn<Medicine, Double> priceColumn;

    @FXML
    private VBox vBoxAdminForm;

    @FXML
    private TextField searchId;
    @FXML
    private TextField searchName;
    @FXML
    private ComboBox<TypeMed> searchComboBoxType;
    @FXML
    private ComboBox<Manufacturer> searchComboBoxManufacturer;
    @FXML
    private DatePicker productionDateSearchPicker;
    @FXML
    private DatePicker expirationDateSearchPicker;
    @FXML
    private TextField searchPrice;

    @FXML
    private TextField nameField;
    @FXML
    private DatePicker productionDateFormPicker;
    @FXML
    private DatePicker expirationDateFormPicker;
    @FXML
    private TextField priceField;
    @FXML
    private Label actionFormLabel;
    @FXML
    private Label actionGuide;
    @FXML
    private Label rowCount;

    @FXML
    private ComboBox<TypeMed> typeComboBox;
    @FXML
    private ComboBox<Manufacturer> manufacturerComboBox;

    @FXML
    private Label usernameIndicator;
    @FXML
    private Label userRoleIndicator;

    private final MedicineService medicineService = MedicineService.getInstance();
    private final ManufacturerService manufacturerService = ManufacturerService.getInstance();
    private final TypeService typeService  = TypeService.getInstance();
    private final SceneSwitcher sceneSwitcher = SceneSwitcher.getInstance();
    private AuthManager authManager = AuthManager.getInstance();

    private Medicine selectedMedicine;



    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().getName()));
        manufacturerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getManufacturer().getName()));
        productionDateColumn.setCellValueFactory(new PropertyValueFactory<>("productionDate"));
        expirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        setAccessToAdminForm();
        loadMedicines();
        loadTypes(authManager.getRole());
        loadManufacturers(authManager.getRole());
        updateRowCount();
        setNumericListeners();
        setTableSelectionListener();
        usernameIndicator.setText("Пользователь: " + authManager.getUsername());
        userRoleIndicator.setText("Роль: " + authManager.getRole());
    }

    private void setNumericListeners(){
        searchId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                searchId.setText(oldValue);
            }
        });
        searchPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                searchPrice.setText(oldValue);
            }
        });
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.equals("0")) {
                priceField.setText(oldValue);
            }
        });
    }

    private void setTableSelectionListener() {
        medicineTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                selectedMedicine = newValue;
                actionGuide.setText("Выберите действие (редактировать/удалить)");
            }
        });
    }

    public void loadMedicines() {
        medicineTable.setItems(medicineService.loadMedicines());
    }

    public void loadTypes(String role) {
        ObservableList<TypeMed> medTypes = typeService.load();
        if (medTypes != null) {
            if(role.equals("ROLE_ADMIN")) {
                typeComboBox.setItems(medTypes);
                typeComboBox.setCellFactory(param -> new ListCell<TypeMed>() {
                    @Override
                    protected void updateItem(TypeMed item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName());
                        } else {
                            setText(null);
                        }
                    }
                });
                typeComboBox.setButtonCell(new ListCell<TypeMed>() {
                    @Override
                    protected void updateItem(TypeMed item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName());
                        } else {
                            setText(null);
                        }
                    }
                });
            }
            searchComboBoxType.setItems(medTypes);
            searchComboBoxType.setCellFactory(param -> new ListCell<TypeMed>() {
                @Override
                protected void updateItem(TypeMed item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.getName());
                    } else {
                        setText(null);
                    }
                }
            });
            searchComboBoxType.setButtonCell(new ListCell<TypeMed>() {
                @Override
                protected void updateItem(TypeMed item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.getName());
                    } else {
                        setText(null);
                    }
                }
            });

        }
    }

    public void loadManufacturers(String role) {
        List<Manufacturer> manufacturers = manufacturerService.load(AuthManager.getInstance().getBasicAuth());
        if (manufacturers != null) {
            ObservableList<Manufacturer> manufacturersObservable = FXCollections.observableArrayList(manufacturers);

            if(role.equals("ROLE_ADMIN")) {
                manufacturerComboBox.setItems(manufacturersObservable);
                manufacturerComboBox.setCellFactory(param -> new ListCell<Manufacturer>() {
                    @Override
                    protected void updateItem(Manufacturer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName());
                        } else {
                            setText(null);
                        }
                    }
                });
                manufacturerComboBox.setButtonCell(new ListCell<Manufacturer>() {
                    @Override
                    protected void updateItem(Manufacturer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName());
                        } else {
                            setText(null);
                        }
                    }
                });
            }

            searchComboBoxManufacturer.setItems(manufacturersObservable);
            searchComboBoxManufacturer.setCellFactory(param -> new ListCell<Manufacturer>() {
                @Override
                protected void updateItem(Manufacturer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.getName());
                    } else {
                        setText(null);
                    }
                }
            });
            searchComboBoxManufacturer.setButtonCell(new ListCell<Manufacturer>() {
                @Override
                protected void updateItem(Manufacturer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.getName());
                    } else {
                        setText(null);
                    }
                }
            });
        }
    }

    public void addMedicine(){
        Medicine newMedicine = new Medicine();
        newMedicine.setName(nameField.getText());
        newMedicine.setType(typeComboBox.getValue());
        newMedicine.setManufacturer(manufacturerComboBox.getValue());
        newMedicine.setProductionDate(String.valueOf(productionDateFormPicker.getValue()));
        newMedicine.setExpirationDate(String.valueOf(expirationDateFormPicker.getValue()));
        newMedicine.setPrice(Integer.valueOf(priceField.getText()));

        try {
            medicineService.add(newMedicine);
            actionGuide.setText("Выберите запись для изменения/удаления");
            actionFormLabel.setText("Форма для добавления и редактирования");
            updateRowCount();
            loadMedicines();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка обновления. Попробуйте снова.");
        }
    }


    @FXML
    private void fillFormForEdit() {
        if (selectedMedicine != null) {
            actionGuide.setText("Отредактируйте данные в форме внизу");
            actionFormLabel.setText("Редактирование");

            nameField.setText(selectedMedicine.getName());
            typeComboBox.setValue(selectedMedicine.getType());
            manufacturerComboBox.setValue(selectedMedicine.getManufacturer());
            productionDateFormPicker.setValue(LocalDate.parse(selectedMedicine.getProductionDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            expirationDateFormPicker.setValue(LocalDate.parse(selectedMedicine.getExpirationDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            priceField.setText(String.valueOf(selectedMedicine.getPrice()));
        }else{
            actionGuide.setText("Выберите запись для изменения/удаления!");
        }
    }

    @FXML
    public void saveMedicine() {
        try {
            checkForm();
            if (selectedMedicine != null) {
                updateMedicine();
                clearForm();
            }else {
                addMedicine();
                clearForm();
            }
        }catch (Exception e) {
            showFormWarning(e.getMessage());
        }
    }
    @FXML
    public void filterSearch(){
        List<Medicine> filteredMedicines= medicineService.search(searchId.getText(),
                searchName.getText(), searchComboBoxType.getValue(), searchComboBoxManufacturer.getValue(), productionDateSearchPicker.getValue(),
                expirationDateSearchPicker.getValue(), searchPrice.getText());
        if (filteredMedicines != null) {
            ObservableList<Medicine> observableList = FXCollections.observableArrayList(filteredMedicines);
            medicineTable.setItems(observableList);
            updateRowCount();
        }
    }
    @FXML
    public void clearFilter(){
        searchId.clear();
        searchName.clear();
        searchComboBoxType.valueProperty().set(null);
        searchComboBoxManufacturer.valueProperty().set(null);
        productionDateSearchPicker.setValue(null);
        expirationDateSearchPicker.setValue(null);
        searchPrice.clear();
        loadMedicines();
        updateRowCount();
    }


    public void updateMedicine(){
        Medicine updatedMedicine = new Medicine();
        updatedMedicine.setId(selectedMedicine.getId());  // Копируем ID
        updatedMedicine.setName(nameField.getText());  // Устанавливаем имя из текстового поля
        updatedMedicine.setType(typeComboBox.getValue());  // Устанавливаем тип из ComboBox
        updatedMedicine.setManufacturer(manufacturerComboBox.getValue());  // Устанавливаем производителя из ComboBox
        updatedMedicine.setProductionDate(String.valueOf(productionDateFormPicker.getValue()));updatedMedicine.setExpirationDate(String.valueOf(expirationDateFormPicker.getValue()));  // Устанавливаем срок годности
        updatedMedicine.setPrice(Integer.valueOf(priceField.getText()));  // Устанавливаем цену
        selectedMedicine = null;
        try {
            medicineService.updateMedicine(updatedMedicine);
            loadMedicines();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка обновления. Попробуйте снова.");
        }
    }

    @FXML
    private void handleAddAction() {
        actionGuide.setText("Заполните данными форму внизу");
        actionFormLabel.setText("Добавление");
    }

    @FXML
    private void clearForm() {
        actionGuide.setText("Выберите запись для изменения/удаления");
        actionFormLabel.setText("Форма для добавления и редактирования");
        nameField.clear();
        typeComboBox.valueProperty().set(null);
        manufacturerComboBox.valueProperty().set(null);
        productionDateFormPicker.valueProperty().set(null);
        expirationDateFormPicker.valueProperty().set(null);
        priceField.clear();
    }

    private void checkForm() throws Exception{
        if (nameField.getText().isEmpty()) {
            throw new Exception("Введите название лекарства");
        }
        if (typeComboBox.getValue() == null) {
            throw new Exception("Введите тип лекарства");
        }
        if (typeComboBox.getValue() == null) {
            throw new Exception("Введите производителя лекарства");
        }
        if (productionDateFormPicker.getValue() == null) {
            throw new Exception("Введите дату производства лекарства");
        }
        if (expirationDateFormPicker.getValue() == null) {
            throw new Exception("Введите дату истечения срока годности лекарства");
        }
        if (priceField.getText().isEmpty()) {
            throw new Exception("Введите цену лекарства");
        }
    }

    private void showFormWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText("Все поля должны быть заполнены");
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void showDeleteWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Удаление невозможно");
        alert.setHeaderText("Ошибка удаления");
        alert.setContentText("Лекарство не может быть удалено, так как оно содержится в других таблицах.");

        alert.showAndWait();
    }

    @FXML
    private void showDeleteConfirmation(){
        if (selectedMedicine != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления");
            alert.setHeaderText("Вы уверены, что хотите удалить запись?");
            alert.setContentText("Id: " + selectedMedicine.getId());
            alert.setContentText("Название: " + selectedMedicine.getName());


            ButtonType okButton = new ButtonType("ОК", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, cancelButton);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == okButton) {
                    Integer responseMessage = medicineService.deleteMedicine(selectedMedicine.getId()); // Обновляем таблицу
                    selectedMedicine = null;
                    if(responseMessage == 200){
                        loadMedicines();
                    } else if (responseMessage == 409) {
                        showDeleteWarning();
                        updateRowCount();
                    }
                }
            });
        }else {
            actionGuide.setText("Выберите запись для изменения/удаления!");
        }

    }

    private void updateRowCount() {
        int rowNumber = medicineTable.getItems().size();
        rowCount.setText("Количество записей: " + rowNumber);
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
        // Убираем VBox из макета
        vBoxAdminForm.setVisible("ROLE_ADMIN".equals(authManager.getRole())); // Учитываем VBox в макете
    }

}

