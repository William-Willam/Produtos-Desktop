package com.exemplo.crudprodutos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // FXMLLoader lê o MainView.fxml e constrói a tela
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/exemplo/crudprodutos/MainView.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("CRUD de Produtos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}