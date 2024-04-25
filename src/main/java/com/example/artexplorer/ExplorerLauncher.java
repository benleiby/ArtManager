package com.example.artexplorer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class ExplorerLauncher extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ExplorerLauncher.class.getResource("implicit-view.fxml"));
        stage.setTitle("The Gallery");
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}