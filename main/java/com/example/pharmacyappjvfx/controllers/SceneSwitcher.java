package com.example.pharmacyappjvfx.controllers;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneSwitcher {
    private boolean toOutAuthFlag = true;
    private static SceneSwitcher instance;
    private Stage stage;
    private Scene mainScene;

    private SceneSwitcher() {
    }

    public void initialize(Stage stage) {
        this.stage = stage;
    }

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
                    Scene authScene = new Scene(root, 400, 600);
                    stage.setScene(authScene);
                    toOutAuthFlag = true;
                }
            }
            stage.show();
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

}
