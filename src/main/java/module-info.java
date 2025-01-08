module org.foodust.searchforlog {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.foodust.searchforlog to javafx.fxml;
    exports org.foodust.searchforlog;
}