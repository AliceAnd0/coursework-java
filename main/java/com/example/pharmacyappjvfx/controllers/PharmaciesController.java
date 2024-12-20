package com.example.pharmacyappjvfx.controllers;


import com.example.pharmacyappjvfx.models.Pharmacy;
import com.example.pharmacyappjvfx.services.AuthManager;
import com.example.pharmacyappjvfx.services.PharmacyService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;

public class PharmaciesController {
    @FXML
    private Label usernameIndicator;
    @FXML
    private Label userRoleIndicator;

    @FXML
    private TextField searchAny;

    @FXML
    private TableView<Pharmacy> pharmacyTable;
    @FXML
    private TableColumn<Pharmacy, Integer> idColumn;
    @FXML
    private TableColumn<Pharmacy, String> addressColumn;
    @FXML
    private TableColumn<Pharmacy, String> phoneNumberColumn;

    @FXML
    private VBox vBoxAdminForm;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneNumberField;

    @FXML
    private Label actionFormLabel;
    @FXML
    private Label actionGuide;
    @FXML
    private Label rowCount;

    private final PharmacyService pharmacyService = PharmacyService.getInstance();
    private final SceneSwitcher sceneSwitcher = SceneSwitcher.getInstance();
    private AuthManager authManager = AuthManager.getInstance();

    private Pharmacy selectedPharmacy;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        setAccessToAdminForm();
        loadPharmacies();
        setTableSelectionListener();
        setPhoneListener();
        updateRowCount();
        usernameIndicator.setText("Пользователь: " + authManager.getUsername());
        userRoleIndicator.setText("Роль: " + authManager.getRole());
    }

    private void setPhoneListener() {
        phoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneNumberField.setText(oldValue);
            }
        });
    }

    private void setTableSelectionListener() {
        pharmacyTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                selectedPharmacy = newValue;
                actionGuide.setText("Выберите действие (редактировать/удалить)");
            }
        });
    }

    public void loadPharmacies() {
        ObservableList<Pharmacy> observableList = pharmacyService.load();
        if (observableList != null) {
            pharmacyTable.setItems(observableList);
        }
    }

    @FXML
    private void fillFormForEdit() {
        if (selectedPharmacy != null) {
            actionGuide.setText("Отредактируйте данные в форме");
            actionFormLabel.setText("Редактирование");

            addressField.setText(selectedPharmacy.getAddress());
            phoneNumberField.setText(selectedPharmacy.getPhoneNumber());
        }else {
            actionGuide.setText("Выберите запись для изменения/удаления!");
        }
    }


    public void addPharmacy() {
        Pharmacy newPharmacy = new Pharmacy();
        newPharmacy.setAddress(addressField.getText());
        newPharmacy.setPhoneNumber(phoneNumberField.getText());

        try {
            pharmacyService.add(newPharmacy);
            actionGuide.setText("Выберите запись для изменения/удаления");
            actionFormLabel.setText("Форма для добавления и редактирования");
            loadPharmacies();
            updateRowCount();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка добавления. Попробуйте снова.");
        }
    }

    @FXML
    public void savePharmacy() {
        try {
            checkForm();
            if (selectedPharmacy != null) {
                updatePharmacy();
                clearForm();
            } else {
                addPharmacy();
                clearForm();
            }
        } catch (Exception e) {
            showFormWarning(e.getMessage());
        }
    }

    public void updatePharmacy() {
        Pharmacy updatedPharmacy = new Pharmacy();
        updatedPharmacy.setId(selectedPharmacy.getId());
        updatedPharmacy.setAddress(addressField.getText());
        updatedPharmacy.setPhoneNumber(phoneNumberField.getText());
        selectedPharmacy = null;
        try {
            pharmacyService.update(updatedPharmacy);
            loadPharmacies();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка обновления. Попробуйте снова.");
        }
    }

    @FXML
    private void handleAddAction() {
        actionGuide.setText("Заполните данные в форме внизу");
        actionFormLabel.setText("Добавление");
    }

    @FXML
    private void clearForm() {
        actionGuide.setText("Выберите запись для изменения/удаления");
        actionFormLabel.setText("Форма для добавления и редактирования");
        addressField.clear();
        phoneNumberField.clear();
    }

    private void checkForm() throws Exception {
        if (addressField.getText().isEmpty()) {
            throw new Exception("Введите адрес аптеки");
        }
        if (phoneNumberField.getText().isEmpty()) {
            throw new Exception("Введите номер телефона аптеки");
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
        alert.setContentText("Аптека не может быть удалена, так как она содержится в других таблицах.");

        alert.showAndWait();
    }

    @FXML
    private void showDeleteConfirmation() {
        if (selectedPharmacy != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления");
            alert.setHeaderText("Вы уверены, что хотите удалить запись?");
            alert.setContentText("Адрес: " + selectedPharmacy.getAddress());

            ButtonType okButton = new ButtonType("ОК", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, cancelButton);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == okButton) {
                    Integer responseM = pharmacyService.delete(selectedPharmacy.getId());
                    selectedPharmacy = null;
                    if (responseM == 200) {
                        loadPharmacies();
                        updateRowCount();
                    }else if (responseM == 409) {
                        showDeleteWarning();
                    }

                }
            });
        } else {
            actionGuide.setText("Выберите запись для изменения/удаления!");
        }
    }

    @FXML
    public void clearFilter() {
        searchAny.clear();
        loadPharmacies();
        updateRowCount();
    }

    @FXML
    public void filterSearch() {
        ObservableList<Pharmacy> filteredPharmacies = pharmacyService.search(searchAny.getText());
        if (filteredPharmacies != null) {
            pharmacyTable.setItems(filteredPharmacies);
            updateRowCount();
        }
    }

    private void updateRowCount() {
        int rowNumber = pharmacyTable.getItems().size();
        rowCount.setText("Количество аптек: " + rowNumber);
    }

    @FXML
    public void switchToManufacturers() {
        sceneSwitcher.switchTo("/views/manufacturers.fxml", false);
    }

    @FXML
    public void switchToMedicines() {
        sceneSwitcher.switchTo("/views/medicines.fxml", false);
    }

    @FXML
    public void switchToSales() {
        sceneSwitcher.switchTo("/views/sales.fxml", false);
    }

    @FXML
    public void switchToStock() {
        sceneSwitcher.switchTo("/views/stock.fxml", false);
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
        sceneSwitcher.switchTo("/views/authentication.fxml", true);
    }

    private void setAccessToAdminForm() {
        if ("ROLE_ADMIN".equals(authManager.getRole())) {
            vBoxAdminForm.setVisible(true);
        } else {
            vBoxAdminForm.setVisible(false);
        }
    }
}
