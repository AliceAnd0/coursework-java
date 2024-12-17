package com.example.pharmacyappjvfx.controllers;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {
    private boolean toOutAuthFlag = true;
    private static SceneSwitcher instance;
    private Stage stage;
    private Scene mainScene;
    private Scene authScene;

    // Приватный конструктор для синглтона
    private SceneSwitcher() {
    }

    // Метод для инициализации с передачей Stage
    public void initialize(Stage stage) {
        this.stage = stage;
        stage.setScene(authScene);
    }

    // Метод для получения единственного экземпляра SceneSwitcher
    public static SceneSwitcher getInstance() {
        if (instance == null) {
            instance = new SceneSwitcher();
        }
        return instance;
    }

    public void switchTo(String fxml, boolean isSwitchToAuth){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            if (toOutAuthFlag) {
                this.mainScene = new Scene(root);
                stage.setScene(mainScene);
                toOutAuthFlag = false;
            }else {
                if (!isSwitchToAuth) {
                    stage.getScene().setRoot(root);
                } else {
                    stage.setScene(authScene);
                }
            }
            stage.show();
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }


}
