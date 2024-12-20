package com.example.pharmacyappjvfx.controllers;

import com.example.pharmacyappjvfx.models.Medicine;
import com.example.pharmacyappjvfx.models.Pharmacy;
import com.example.pharmacyappjvfx.models.Sales;
import com.example.pharmacyappjvfx.services.AuthManager;
import com.example.pharmacyappjvfx.services.MedicineService;
import com.example.pharmacyappjvfx.services.PharmacyService;
import com.example.pharmacyappjvfx.services.SalesService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SalesController {

    @FXML
    private TableView<Sales> salesTable;
    @FXML
    private TableColumn<Sales, Integer> idColumn;
    @FXML
    private TableColumn<Sales, String> dateColumn;
    @FXML
    private TableColumn<Sales, String> pharmacyColumn;
    @FXML
    private TableColumn<Sales, Double> amountColumn;
    @FXML
    private TableColumn<Sales, Integer> medicineIdColumn;
    @FXML
    private TableColumn<Sales, String> medicineNameColumn;
    @FXML
    private TableColumn<Sales, Integer> sumColumn;

    @FXML
    private VBox vBoxAdminForm;

    @FXML
    private TextField searchId;
    @FXML
    private ComboBox<Pharmacy> searchComboBoxPharmacy;
    @FXML
    private ComboBox<Medicine> searchComboBoxMedicine;
    @FXML
    private DatePicker searchDatePicker;
    @FXML
    private TextField searchAmount;
    @FXML
    private TextField searchSum;

    @FXML
    private TextField customerField;
    @FXML
    private DatePicker saleDateFormPicker;
    @FXML
    private ComboBox<Medicine> medicineFormComboBox;
    @FXML
    private ComboBox<Pharmacy> pharmacyFormComboBox;
    @FXML
    private TextField amountField;
    @FXML
    private Label sumLabel;

    @FXML
    private Label actionFormLabel;
    @FXML
    private Label actionGuide;
    @FXML
    private Label rowCount;

    @FXML
    private Label usernameIndicator;
    @FXML
    private Label userRoleIndicator;

    private final SalesService saleService = SalesService.getInstance();
    private final MedicineService medicineService = MedicineService.getInstance();
    private final PharmacyService pharmacyService = PharmacyService.getInstance();
    private final SceneSwitcher sceneSwitcher = SceneSwitcher.getInstance();
    private AuthManager authManager = AuthManager.getInstance();

    private Sales selectedSale;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        pharmacyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPharmacy().getAddress()));
        medicineIdColumn.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        medicineNameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));

        setAccessToAdminForm();
        loadSales();
        loadPharmacies(authManager.getRole());
        loadMedicines(authManager.getRole());

        updateRowCount();
        setNumericListeners();
        setTableSelectionListener();
        usernameIndicator.setText("Пользователь: " + authManager.getUsername());
        userRoleIndicator.setText("Роль: " + authManager.getRole());
    }

    private void setNumericListeners() {
        searchId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                searchId.setText(oldValue);
            }
        });
        searchAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                searchAmount.setText(oldValue);
            }
        });
        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.equals("0")) {
                amountField.setText(oldValue);
            }
            ;
            if(medicineFormComboBox.getValue()!=null) {
                int medPrice = medicineFormComboBox.getValue().getPrice();
                sumLabel.setText(String.valueOf(medPrice * Integer.parseInt(newValue)));
            }

        });
        searchSum.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                amountField.setText(oldValue);
            }
        });
    }

    private void setTableSelectionListener() {
        salesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                selectedSale = newValue;
                actionGuide.setText("Выберите действие (редактировать/удалить)");
            }
        });
    }

    public void loadSales() {
        salesTable.setItems(saleService.loadSales());
    }

    public void loadMedicines(String role) {
        ObservableList<Medicine> medicines = medicineService.loadMedicines();
        if (medicines != null) {
            if (role.equals("ROLE_ADMIN")) {
                medicineFormComboBox.setItems(medicines);
                medicineFormComboBox.setCellFactory(param -> new ListCell<Medicine>() {
                    @Override
                    protected void updateItem(Medicine item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName());
                        } else {
                            setText(null);
                        }
                    }
                });
                medicineFormComboBox.setButtonCell(new ListCell<Medicine>() {
                    @Override
                    protected void updateItem(Medicine item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName());
                        } else {
                            setText(null);
                        }
                    }
                });
            }

            searchComboBoxMedicine.setItems(medicines);
            searchComboBoxMedicine.setCellFactory(param -> new ListCell<Medicine>() {
                @Override
                protected void updateItem(Medicine item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.getName());
                    } else {
                        setText(null);
                    }
                }
            });
            searchComboBoxMedicine.setButtonCell(new ListCell<Medicine>() {
                @Override
                protected void updateItem(Medicine item, boolean empty) {
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
    public void loadPharmacies(String role) {
        ObservableList<Pharmacy> pharmacies = pharmacyService.load();
        if (pharmacies != null) {
            if (role.equals("ROLE_ADMIN")) {
                pharmacyFormComboBox.setItems(pharmacies);
                pharmacyFormComboBox.setCellFactory(param -> new ListCell<Pharmacy>() {
                    @Override
                    protected void updateItem(Pharmacy item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getAddress());
                        } else {
                            setText(null);
                        }
                    }
                });
                pharmacyFormComboBox.setButtonCell(new ListCell<Pharmacy>() {
                    @Override
                    protected void updateItem(Pharmacy item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getAddress());
                        } else {
                            setText(null);
                        }
                    }
                });
            }

            searchComboBoxPharmacy.setItems(pharmacies);
            searchComboBoxPharmacy.setCellFactory(param -> new ListCell<Pharmacy>() {
                @Override
                protected void updateItem(Pharmacy item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.getAddress());
                    } else {
                        setText(null);
                    }
                }
            });
            searchComboBoxPharmacy.setButtonCell(new ListCell<Pharmacy>() {
                @Override
                protected void updateItem(Pharmacy item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.getAddress());
                    } else {
                        setText(null);
                    }
                }
            });
        }
    }


    public void addSale() {
        Sales newSale = new Sales();
        newSale.setPharmacy(pharmacyFormComboBox.getValue());
        newSale.setMedicineId(medicineFormComboBox.getValue().getId());
        newSale.setMedicineName(medicineFormComboBox.getValue().getName());
        newSale.setDate(String.valueOf(saleDateFormPicker.getValue()));
        newSale.setAmount(Integer.parseInt(amountField.getText()));
        newSale.setSum(Integer.parseInt(sumLabel.getText()));

        try {
            saleService.add(newSale);
            loadSales();
            updateRowCount();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка обновления. Попробуйте снова.");
        }
    }

    @FXML
    private void fillFormForEdit() {
        if (selectedSale != null) {
            actionGuide.setText("Отредактируйте данные в форме внизу");
            actionFormLabel.setText("Редактирование");

            pharmacyFormComboBox.setValue(selectedSale.getPharmacy());

            medicineFormComboBox.setValue(medicineService.findMedicineById(selectedSale.getMedicineId()));

            saleDateFormPicker.setValue(LocalDate.parse(selectedSale.getDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            amountField.setText(String.valueOf(selectedSale.getAmount()));
            sumLabel.setText(String.valueOf(selectedSale.getSum()));
        }else{
            actionGuide.setText("Выберите запись для изменения/удаления!");
        }
    }

    @FXML
    public void saveSale() {
        try {
            checkForm();
            if (selectedSale != null) {
                updateSale();
                clearForm();
            } else {
                addSale();
                clearForm();
            }
        } catch (Exception e) {
            showFormWarning(e.getMessage());
        }
    }

    @FXML
    public void filterSearch() {
        ObservableList<Sales> observableList= saleService.search(searchId.getText(), searchDatePicker.getValue(),
                searchComboBoxMedicine.getValue(), searchComboBoxMedicine.getValue(), searchComboBoxPharmacy.getValue());
        if (observableList != null) {
            salesTable.setItems(observableList);
            updateRowCount();
        }
    }

    @FXML
    public void clearFilter() {
        searchId.clear();
        searchDatePicker.setValue(null);
        searchComboBoxPharmacy.setValue(null);
        searchComboBoxMedicine.setValue(null);
        searchAmount.clear();
        searchSum.clear();
        loadSales();
        updateRowCount();
    }

    public void updateSale() {
        Sales updatedSale = new Sales();
        updatedSale.setId(selectedSale.getId());
        updatedSale.setPharmacy(pharmacyFormComboBox.getValue());
        updatedSale.setMedicineId(medicineFormComboBox.getValue().getId());
        updatedSale.setMedicineName(medicineFormComboBox.getValue().getName());
        updatedSale.setDate(String.valueOf(saleDateFormPicker.getValue()));
        updatedSale.setAmount(Integer.parseInt(amountField.getText()));
        updatedSale.setSum(Integer.parseInt(sumLabel.getText()));
        selectedSale = null;
        try {
            saleService.update(updatedSale);
            loadSales();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка обновления. Попробуйте снова.");
        }
    }

    @FXML
    private void handleAddAction() {
        actionGuide.setText("Заполните данные в форме внизу");
    }


    @FXML
    private void clearForm() {
        actionGuide.setText("Выберите запись для изменения/удаления");
        actionFormLabel.setText("Форма для добавления и редактирования");
        pharmacyFormComboBox.setValue(null);
        medicineFormComboBox.valueProperty().set(null);
        saleDateFormPicker.valueProperty().set(null);
        amountField.clear();
        sumLabel.setText("Сумма");
    }

    private void checkForm() throws Exception {
        if (pharmacyFormComboBox.getValue() == null) {
            throw new Exception("Введите лекарство");
        }
        if (medicineFormComboBox.getValue() == null) {
            throw new Exception("Введите лекарство");
        }
        if (saleDateFormPicker.getValue() == null) {
            throw new Exception("Введите дату продажи");
        }
        if (amountField.getText().isEmpty()) {
            throw new Exception("Введите количество проданных товаров");
        }
    }

    private void showFormWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText("Все поля должны быть заполнены");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void showDeleteConfirmation() {
        if (selectedSale != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления");
            alert.setHeaderText("Вы уверены, что хотите удалить запись?");
            alert.setContentText("Id: " + selectedSale.getId());
            alert.setContentText("Дата продажи: " + selectedSale.getDate());

            ButtonType okButton = new ButtonType("ОК", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, cancelButton);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == okButton) {
                    Integer responseMessage = saleService.delete(selectedSale.getId()); // Обновляем таблицу
                    if(responseMessage == 200){
                        loadSales();
                        updateRowCount();
                    }
                }
            });
        }else {
            actionGuide.setText("Выберите запись для изменения/удаления!");
        }
    }

    private void updateRowCount() {
        int rowNumber = salesTable.getItems().size();
        rowCount.setText("Количество записей: " + rowNumber);
    }

    @FXML
    public void switchToMedicines() {
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

    private void setAccessToAdminForm() {
        vBoxAdminForm.setVisible("ROLE_ADMIN".equals(authManager.getRole()));
    }
    }
