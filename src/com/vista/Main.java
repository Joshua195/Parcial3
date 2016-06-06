package com.vista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage stagePrincipal;
    private AnchorPane rootPane;


    @Override
    public void start(Stage primaryStage) throws Exception{
        this.stagePrincipal = primaryStage;
        iniciar();
    }

    public void iniciar(){
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("sample.fxml"));
            rootPane = (AnchorPane) loader.load();
            Scene scene = new Scene(rootPane);
            stagePrincipal.setTitle("Parcial 3");
            stagePrincipal.setScene(scene);
            Controller controller = loader.getController();
            controller.setMain(this);
            stagePrincipal.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getStagePrincipal() {
        return stagePrincipal;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
