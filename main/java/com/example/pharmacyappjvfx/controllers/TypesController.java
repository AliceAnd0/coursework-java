package com.example.pharmacyappjvfx.controllers;

import com.example.pharmacyappjvfx.models.TypeMed;
import com.example.pharmacyappjvfx.services.AuthManager;
import com.example.pharmacyappjvfx.services.TypeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;

public class TypesController {

    @FXML
    private Label usernameIndicator;
    @FXML
    private Label userRoleIndicator;

    @FXML
    private TextField searchAny;

    @FXML
    private TableView<TypeMed> typeMedTable;
    @FXML
    private TableColumn<TypeMed, Integer> idColumn;
    @FXML
    private TableColumn<TypeMed, String> nameColumn;

    @FXML
    private VBox vBoxAdminForm;
    @FXML
    private TextField nameField;

    @FXML
    private Label actionFormLabel;
    @FXML
    private Label actionGuide;
    @FXML
    private Label rowCount;

    private final TypeService typeMedService = TypeService.getInstance();
    private final SceneSwitcher sceneSwitcher = SceneSwitcher.getInstance();
    private AuthManager authManager = AuthManager.getInstance();

    private TypeMed selectedTypeMed;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        setAccessToAdminForm();
        loadTypeMeds();
        setTableSelectionListener();
        updateRowCount();
        usernameIndicator.setText("Пользователь: " + authManager.getUsername());
        userRoleIndicator.setText("Роль: " + authManager.getRole());
    }

    private void setTableSelectionListener() {
        typeMedTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                selectedTypeMed = newValue;
                actionGuide.setText("Выберите действие (редактировать/удалить)");
            }
        });
    }

    public void loadTypeMeds() {
        List<TypeMed> typeMeds = typeMedService.load();
        if (typeMeds != null) {
            ObservableList<TypeMed> observableList = FXCollections.observableArrayList(typeMeds);
            typeMedTable.setItems(observableList);
        }
    }

    @FXML
    private void fillFormForEdit() {
        if (selectedTypeMed != null) {
            actionGuide.setText("Отредактируйте данные в форме");
            actionFormLabel.setText("Редактирование");
            nameField.setText(selectedTypeMed.getName());
        }else {
            actionGuide.setText("Выберите запись для изменения/удаления!");
        }
    }

    public void addTypeMed() {
        TypeMed newTypeMed = new TypeMed();
        newTypeMed.setName(nameField.getText());

        try {
            typeMedService.add(newTypeMed);
            loadTypeMeds();
            updateRowCount();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка добавления. Попробуйте снова.");
        }
    }

    @FXML
    public void saveTypeMed() {
        try {
            checkForm();
            if (selectedTypeMed != null) {
                updateTypeMed();
                clearForm();
            } else {
                addTypeMed();
                clearForm();
            }
        } catch (Exception e) {
            showFormWarning(e.getMessage());
        }
    }

    public void updateTypeMed() {
        TypeMed updatedTypeMed = new TypeMed();
        updatedTypeMed.setId(selectedTypeMed.getId());
        updatedTypeMed.setName(nameField.getText());
        selectedTypeMed = null;
        try {
            typeMedService.update(updatedTypeMed);
            loadTypeMeds();
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
        nameField.clear();
    }

    private void checkForm() throws Exception {
        if (nameField.getText().isEmpty()) {
            throw new Exception("Введите название типа лекарства");
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
        alert.setContentText("Тип не может быть удален, так как он содержится в других таблицах.");

        alert.showAndWait();
    }

    @FXML
    private void showDeleteConfirmation() {
        if (selectedTypeMed != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления");
            alert.setHeaderText("Вы уверены, что хотите удалить запись?");
            alert.setContentText("Название: " + selectedTypeMed.getName());

            ButtonType okButton = new ButtonType("ОК", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, cancelButton);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == okButton) {
                    Integer responseMessage = typeMedService.delete(selectedTypeMed.getId());
                    selectedTypeMed = null;
                    if(responseMessage == 200){
                        loadTypeMeds();
                        updateRowCount();
                    } else if (responseMessage == 409) {
                        showDeleteWarning();
                    }
                }
            });
        }else {
            actionGuide.setText("Выберите запись для изменения/удаления!");
        }

    }

    @FXML
    public void clearFilter() {
        searchAny.clear();
        loadTypeMeds();
        updateRowCount();
    }

    @FXML
    public void filterSearch() {
        ObservableList<TypeMed> filteredTypeMeds = typeMedService.search(searchAny.getText());
        if (filteredTypeMeds != null) {
            typeMedTable.setItems(filteredTypeMeds);
            updateRowCount();
        }
    }

    private void updateRowCount() {
        int rowNumber = typeMedTable.getItems().size();
        rowCount.setText("Количество типов: " + rowNumber);
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
    public void switchToStatistics() {
        sceneSwitcher.switchTo("/views/statistics.fxml", false);
    }
    @FXML
    public void switchToAuth(){
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
