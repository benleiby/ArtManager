module com.example.artexplorer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires javafx.swing;
    requires org.lz4.java;
    requires org.slf4j;

    opens com.example.artexplorer to javafx.fxml;
    exports com.example.artexplorer;
}