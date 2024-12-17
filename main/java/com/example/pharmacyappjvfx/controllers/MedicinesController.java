package com.example.pharmacyappjvfx.controllers;

import com.example.pharmacyappjvfx.models.Manufacturer;
import com.example.pharmacyappjvfx.models.Medicine;
import com.example.pharmacyappjvfx.models.TypeMed;
import com.example.pharmacyappjvfx.services.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
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
    private ComboBox<TypeMed> searchComboBoxType;//private DatePicker productionDatePicker; //
    @FXML
    private ComboBox<Manufacturer> searchComboBoxManufacturer;//private DatePicker productionDatePicker; //
    @FXML
    private DatePicker productionDateSearchPicker; //
    @FXML
    private DatePicker expirationDateSearchPicker; //
    @FXML
    private TextField searchPrice;

    @FXML
    private TextField nameField;
    @FXML
    private DatePicker productionDateFormPicker; //
    @FXML
    private DatePicker expirationDateFormPicker; //
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

    private boolean isEditButtonPressed = false;
    private boolean isDeleteButtonPressed = false;

    private final MedicineService medicineService = MedicineService.getInstance();
    private final ManufacturerService manufacturerService = ManufacturerService.getInstance();
    private final TypeService typeService  = TypeService.getInstance();
    private final SceneSwitcher sceneSwitcher = SceneSwitcher.getInstance();
    private final AuthManager authManager = AuthManager.getInstance();

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
            if (!newValue.matches("\\d*")) { // "\\d*" означает только цифры
                searchId.setText(oldValue);
            }
        });
        searchPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // "\\d*" означает только цифры
                searchPrice.setText(oldValue);
            }
        });
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.equals("0")) { // "\\d*" означает только цифры
                priceField.setText(oldValue);
            }
        });
    }

    private void setTableSelectionListener() {
        // Обработчик для выбора строки в таблице
        medicineTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedMedicine = newValue;
            if (isDeleteButtonPressed) {
                isDeleteButtonPressed = false;
                showDeleteConfirmation(newValue);
            } else if (isEditButtonPressed) {
                fillFormForEdit(newValue); // Заполняем форму для редактирования
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
                            setText(item.getName()); // Показываем только имя производителя
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
                            setText(item.getName()); // Показываем только имя производителя
                        } else {
                            setText(null);
                        }  // Отображаем только имя
                    }
                });
            }
            searchComboBoxType.setItems(medTypes);
            searchComboBoxType.setCellFactory(param -> new ListCell<TypeMed>() {
                @Override
                protected void updateItem(TypeMed item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.getName()); // Показываем только имя производителя
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
                        setText(item.getName()); // Показываем только имя производителя
                    } else {
                        setText(null);
                    }  // Отображаем только имя
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
                            setText(item.getName()); // Показываем только имя производителя
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
                            setText(item.getName()); // Показываем только имя производителя
                        } else {
                            setText(null);
                        }  // Отображаем только имя
                    }
                });
            }

            searchComboBoxManufacturer.setItems(manufacturersObservable);
            searchComboBoxManufacturer.setCellFactory(param -> new ListCell<Manufacturer>() {
                @Override
                protected void updateItem(Manufacturer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.getName()); // Показываем только имя производителя
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
                        setText(item.getName()); // Показываем только имя производителя
                    } else {
                        setText(null);
                    }  // Отображаем только имя
                }
            });
        }
    }

    public void addMedicine(){
        Medicine newMedicine = new Medicine();
        newMedicine.setName(nameField.getText());  // Устанавливаем имя из текстового поля
        newMedicine.setType(typeComboBox.getValue());  // Устанавливаем тип из ComboBox
        newMedicine.setManufacturer(manufacturerComboBox.getValue());  // Устанавливаем производителя из ComboBox
        newMedicine.setProductionDate(String.valueOf(productionDateFormPicker.getValue()));  // Устанавливаем дату производства
        newMedicine.setExpirationDate(String.valueOf(expirationDateFormPicker.getValue()));  // Устанавливаем срок годности
        newMedicine.setPrice(Integer.valueOf(priceField.getText()));  // Устанавливаем цену

        try {
            // Попытка обновить medicine через сервисный метод
            medicineService.add(newMedicine);
            updateRowCount();
            loadMedicines(); // Обновляем список лекарств после успешного обновления
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка обновления. Попробуйте снова.");
        }
    }

    @FXML
    public void editMedicine() {
        fillFormForEdit(selectedMedicine);
    }

    private void fillFormForEdit(Medicine medicine) {
        actionGuide.setText("Отредактируйте данные в форме внизу");
        actionFormLabel.setText("Редактирование");

        nameField.setText(medicine.getName());
        typeComboBox.setValue(medicine.getType());
        manufacturerComboBox.setValue(medicine.getManufacturer());
        productionDateFormPicker.setValue(LocalDate.parse(medicine.getProductionDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        //System.out.println("Лекарство:   " + medicine.getProductionDate());//productionDateField.setText(medicine.getProductionDate());

        expirationDateFormPicker.setValue(LocalDate.parse(medicine.getExpirationDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        priceField.setText(String.valueOf(medicine.getPrice()));
    }

    @FXML
    public void saveMedicine() {
        try {
            checkForm();
            if (selectedMedicine != null) {
                updateMedicine();
                isEditButtonPressed = false;
                clearForm();
                loadMedicines(); // Обновляем список лекарств
            }else {
                addMedicine();
                clearForm();
                loadMedicines();
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

        try {
            // Попытка обновить medicine через сервисный метод
            medicineService.updateMedicine(updatedMedicine);
            loadMedicines(); // Обновляем список лекарств после успешного обновления
        } catch (Exception e) {
            e.printStackTrace();
            // Если обновление не удалось, можно использовать копию для отката
            // Например, можно вывести сообщение пользователю о том, что обновление не удалось
            System.out.println("Ошибка обновления. Попробуйте снова.");
        }
    }

    @FXML
    private void handleAddAction() {
        actionGuide.setText("Заполните данными форму внизу");
    }

    @FXML
    private void handleEditAction() {
        isEditButtonPressed = true;
        actionGuide.setText("Выберите запись для редактирования");
    }

    @FXML
    private void handleDeleteAction() {
        isDeleteButtonPressed = true;
        actionGuide.setText("Выберите запись для удаления");
    }

    @FXML
    private void clearForm() {
        actionFormLabel.setText("Форма для добавления и редактирования");
        nameField.clear();
        typeComboBox.valueProperty().set(null);
        manufacturerComboBox.valueProperty().set(null);
        productionDateFormPicker.valueProperty().set(null);//productionDateField.clear();
        expirationDateFormPicker.valueProperty().set(null);
        priceField.clear();
        isDeleteButtonPressed = false;
        actionGuide.setText("Выберите действие");
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

        // Отображаем диалоговое окно и ждем, пока пользователь нажмет "ОК"
        alert.showAndWait();
    }

    private void showDeleteConfirmation(Medicine selectedMedicine){
        //isDeleteButtonPressed = false;
        medicineTable.getSelectionModel().clearSelection();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Вы уверены, что хотите удалить запись?");
        alert.setContentText("Id: " + selectedMedicine.getId());
        alert.setContentText("Название: " + selectedMedicine.getName());

        // Добавляем кнопки "ОК" и "Отмена"
        ButtonType okButton = new ButtonType("ОК", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, cancelButton);

        // Показываем диалог и обрабатываем результат
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == okButton) {
                String responseMessage = medicineService.deleteMedicine(selectedMedicine.getId()); // Обновляем таблицу
                if(responseMessage.startsWith("Лекарство")){
                    loadMedicines();
                }
                actionFormLabel.setText("Запись удалена");
            } else if (buttonType == cancelButton) {
                // Действие отменено
                actionFormLabel.setText("Удаление отменено");
            }
            actionGuide.setText("Выберите действие");
        });
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

