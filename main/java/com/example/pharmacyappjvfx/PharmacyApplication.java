package com.example.pharmacyappjvfx;

//mport com.example.pharmacyappjvfx.controllers.MedicineApiController;
import com.example.pharmacyappjvfx.controllers.SceneSwitcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class PharmacyApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        SceneSwitcher sceneSwitcher = SceneSwitcher.getInstance();
        sceneSwitcher.initialize(stage);
        stage.setTitle("Информационно-справочная система для аптеки");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/authentication.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 400, 600);
        stage.setScene(scene);
        stage.show();
    }

}