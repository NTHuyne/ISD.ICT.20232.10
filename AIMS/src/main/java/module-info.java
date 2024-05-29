module com.hust.ict.aims {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
	requires org.apache.httpcomponents.httpclient;
	requires java.desktop;
	requires net.freeutils.jlhttp;

    opens com.hust.ict.aims to javafx.fxml;
    opens com.hust.ict.aims.view.home to javafx.fxml;
    opens com.hust.ict.aims.view.cart to javafx.fxml;
    opens com.hust.ict.aims.view.place to javafx.fxml;
    exports com.hust.ict.aims;
}