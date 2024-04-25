module com.example.artexplorer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.artexplorer to javafx.fxml;
    exports com.example.artexplorer;
}