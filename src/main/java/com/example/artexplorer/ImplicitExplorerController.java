package com.example.artexplorer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ImplicitExplorerController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}