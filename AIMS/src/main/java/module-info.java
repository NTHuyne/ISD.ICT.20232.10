module com.hust.ict.aims {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.hust.ict.aims to javafx.fxml;
    exports com.hust.ict.aims;
}