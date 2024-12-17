package com.example.pharmacyappjvfx.controllers;


import com.example.pharmacyappjvfx.models.Medicine;
import com.example.pharmacyappjvfx.models.Pharmacy;
import com.example.pharmacyappjvfx.models.Stock;
import com.example.pharmacyappjvfx.services.AuthManager;
import com.example.pharmacyappjvfx.services.MedicineService;
import com.example.pharmacyappjvfx.services.PharmacyService;
import com.example.pharmacyappjvfx.services.StockService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class StockController {

    @FXML
    private TableView<Stock> stockTable;
    @FXML
    private TableColumn<Stock, String> pharmacyNameColumn;
    @FXML
    private TableColumn<Stock, String> medicineNameColumn;
    @FXML
    private TableColumn<Stock, Integer> amountColumn;

    @FXML
    private VBox vBoxAdminForm;

    @FXML
    private ComboBox<Pharmacy> searchComboBoxPharmacy;
    @FXML
    private ComboBox<Medicine> searchComboBoxMedicine;
    @FXML
    private TextField searchQuantity;

    @FXML
    private TextField quantityField;
    @FXML
    private ComboBox<Pharmacy> pharmacyFormComboBox;
    @FXML
    private ComboBox<Medicine> medicineFormComboBox;
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

    private boolean isEditButtonPressed = false;
    private boolean isDeleteButtonPressed = false;

    private final StockService stockService = StockService.getInstance();
    private final MedicineService medicineService = MedicineService.getInstance();
    private final PharmacyService pharmacyService = PharmacyService.getInstance();
    private final SceneSwitcher sceneSwitcher = SceneSwitcher.getInstance();
    private final AuthManager authManager = AuthManager.getInstance();

    private ChangeListener<Stock> tableSelectionListener;
    private boolean isUpdatingTable;
    private Stock selectedStock;

    @FXML
    public void initialize() {
        pharmacyNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPharmacy().getAddress()));
        medicineNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMedicine().getName()));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        setAccessToAdminForm();
        loadStock();
        loadMedicines(authManager.getRole());
        loadPharmacies(authManager.getRole());

        updateRowCount();
        setNumericListeners();
        setTableSelectionListener();
        usernameIndicator.setText("Пользователь: " + authManager.getUsername());
        userRoleIndicator.setText("Роль: " + authManager.getRole());
        actionGuide.setText("Выберите действие");
    }

    private void setNumericListeners() {
        searchQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // "\\d*" означает только цифры
                searchQuantity.setText(oldValue);
            }
        });
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.equals("0")) { // "\\d*" означает только цифры
                quantityField.setText(oldValue);
            }
        });
    }

    private void setTableSelectionListener() {
        stockTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null) {
                selectedStock = null;
                return;
            }
            selectedStock = newValue;
            if (isDeleteButtonPressed) {
                isDeleteButtonPressed = false;
                showDeleteConfirmation(newValue);
            } else if (isEditButtonPressed) {
                fillFormForEdit(newValue);
            }
        });
    }

    public void loadStock() {
        ObservableList<Stock> stocks = stockService.loadStocks();
        stockTable.setItems(stocks);
    }

    public void loadMedicines(String role) {
        ObservableList<Medicine> medicines = medicineService.loadMedicines();
        if (medicines != null) {
            if(role.equals("ROLE_ADMIN")) {
                // Для роли ADMIN используем pharmacyFormComboBox для аптеки
                medicineFormComboBox.setItems(medicines);
                medicineFormComboBox.setCellFactory(param -> new ListCell<Medicine>() {
                    @Override
                    protected void updateItem(Medicine item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName()); // Показываем только имя лекарства
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
                            setText(item.getName()); // Показываем только имя лекарства
                        } else {
                            setText(null);
                        }
                    }
                });
            }

            // Общая часть, если роль не ADMIN
            searchComboBoxMedicine.setItems(medicines);
            searchComboBoxMedicine.setCellFactory(param -> new ListCell<Medicine>() {
                @Override
                protected void updateItem(Medicine item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.getName()); // Показываем только имя лекарства
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
                        setText(item.getName()); // Показываем только имя лекарства
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
            if(role.equals("ROLE_ADMIN")) {
                // Для роли ADMIN используем pharmacyFormComboBox для аптеки
                pharmacyFormComboBox.setItems(pharmacies);
                pharmacyFormComboBox.setCellFactory(param -> new ListCell<Pharmacy>() {
                    @Override
                    protected void updateItem(Pharmacy item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getAddress()); // Показываем адрес аптеки
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
                            setText(item.getAddress()); // Показываем адрес аптеки
                        } else {
                            setText(null);
                        }
                    }
                });
            }

            // Общая часть для поиска аптек, если роль не ADMIN
            searchComboBoxPharmacy.setItems(pharmacies);
            searchComboBoxPharmacy.setCellFactory(param -> new ListCell<Pharmacy>() {
                @Override
                protected void updateItem(Pharmacy item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.getAddress()); // Показываем адрес аптеки
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
                        setText(item.getAddress()); // Показываем адрес аптеки
                    } else {
                        setText(null);
                    }
                }
            });
        }
    }

    public void addStock() {
        Stock newStock = new Stock();
        newStock.setPharmacy(pharmacyFormComboBox.getValue());
        newStock.setMedicine(medicineFormComboBox.getValue());
        newStock.setAmount(Integer.parseInt(quantityField.getText()));

        try {
            stockService.add(newStock);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка добавления. Попробуйте снова.");
        }
    }

    private void fillFormForEdit(Stock stock) {
        actionGuide.setText("Отредактируйте данные в форме внизу");
        actionFormLabel.setText("Редактирование");

        pharmacyFormComboBox.setValue(stock.getPharmacy());
        medicineFormComboBox.setValue(stock.getMedicine());
        quantityField.setText(String.valueOf(stock.getAmount()));
    }

    @FXML
    public void saveStock() {
        try {
            checkForm();
            if (selectedStock != null) {
                updateStock();
                isEditButtonPressed = false;
                clearForm();
                loadStock();
            } else {
                addStock();
                clearForm();
                loadStock();
                updateRowCount();
            }
        } catch (Exception e) {
            showFormWarning(e.getMessage());
        }
    }

    @FXML
    public void filterSearch() {
        ObservableList<Stock> filteredStock = stockService.search(searchComboBoxMedicine.getValue(), searchComboBoxPharmacy.getValue(),
                searchQuantity.getText());
        if (filteredStock != null) {
            stockTable.setItems(filteredStock);
            updateRowCount();
        }
    }

    @FXML
    public void clearFilter() {
        searchComboBoxPharmacy.setValue(null);
        searchComboBoxMedicine.setValue(null);
        searchQuantity.clear();
        loadStock();
        updateRowCount();
    }

    public void updateStock() {
        Stock updatedStock = new Stock();
        updatedStock.setPk(selectedStock.getPk());
        updatedStock.setPharmacy(pharmacyFormComboBox.getValue());
        updatedStock.setMedicine(medicineFormComboBox.getValue());
        updatedStock.setAmount(Integer.parseInt(quantityField.getText()));

        try {
            stockService.update(updatedStock);
            loadStock();
        } catch (Exception e) {
            e.printStackTrace();
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
        pharmacyFormComboBox.setValue(null);
        medicineFormComboBox.valueProperty().set(null);
        quantityField.clear();
        isDeleteButtonPressed = false;
        actionGuide.setText("Выберите действие");
    }

    private void checkForm() throws Exception {
        if (pharmacyFormComboBox.getValue() == null) {
            throw new Exception("Выберите аптеку");
        }
        if (medicineFormComboBox.getValue() == null) {
            throw new Exception("Выберите лекарство");
        }
        if (quantityField.getText().isEmpty()) {
            throw new Exception("Введите количество");
        }
    }

    private void showFormWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText("Все поля должны быть заполнены");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showDeleteConfirmation(Stock selectedStock) {
        stockTable.getSelectionModel().clearSelection();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Вы уверены, что хотите удалить запись?");
        alert.setContentText("Аптека: " + selectedStock.getPharmacy().getId() + "-" + selectedStock.getPharmacy().getAddress());
        alert.setContentText("Лекарство: " + selectedStock.getMedicine().getId() + "-" + selectedStock.getMedicine().getName());
        alert.setContentText("Количество: " + selectedStock.getAmount());

        ButtonType okButton = new ButtonType("ОК", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, cancelButton);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == okButton) {
                String responseMessage = stockService.delete(selectedStock.getMedicine().getId(), selectedStock.getPharmacy().getId());
                if(responseMessage.startsWith("Лекарство")){
                    loadStock();
                    updateRowCount();
                }
            }else if (buttonType == cancelButton) {
                actionFormLabel.setText("Удаление отменено");
            }
            actionGuide.setText("Выберите действие");
        });
    }

    private void updateRowCount() {
        int rowNumber = stockTable.getItems().size();
        rowCount.setText("Количество записей: " + rowNumber);
    }


    @FXML
    public void switchToManufacturers() {
        sceneSwitcher.switchTo("/views/manufacturers.fxml", false);
    }

    @FXML
    public void switchToPharmacies() {
        sceneSwitcher.switchTo("/views/pharmacies.fxml", false);
    }

    @FXML
    public void switchToSales() {
        sceneSwitcher.switchTo("/views/sales.fxml", false);
    }

    @FXML
    public void switchToTypes() {
        sceneSwitcher.switchTo("/views/types.fxml", false);
    }

    @FXML
    public void switchToAuthor() {
        sceneSwitcher.switchTo("/views/author.fxml", false);
    }

    @FXML
    public void switchToStatistics() {
        sceneSwitcher.switchTo("/views/statistics.fxml", false);
    }

    @FXML
    public void switchToAuth() {
        sceneSwitcher.switchTo("/views/authentication.fxml", false);
    }
    @FXML
    public void switchToMedicines(){
        sceneSwitcher.switchTo("/views/medicines.fxml", false);
    }

    private void setAccessToAdminForm() {
        vBoxAdminForm.setVisible("ROLE_ADMIN".equals(authManager.getRole()));
    }
}
