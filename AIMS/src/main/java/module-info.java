module com.hust.ict.aims {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
	requires org.apache.httpcomponents.httpclient;

    opens com.hust.ict.aims to javafx.fxml;
    exports com.hust.ict.aims;
}