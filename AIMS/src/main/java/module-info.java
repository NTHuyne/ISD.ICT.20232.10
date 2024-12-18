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
	requires javafx.graphics;
//    requires org.simplejavamail.core;
    requires org.simplejavamail;


    opens com.hust.ict.aims to javafx.fxml;
    opens com.hust.ict.aims.view.home to javafx.fxml;
    opens com.hust.ict.aims.view.cart to javafx.fxml;
    opens com.hust.ict.aims.view.place to javafx.fxml;
    opens com.hust.ict.aims.view.login to javafx.fxml;
    opens com.hust.ict.aims.view.order to javafx.fxml;
    opens com.hust.ict.aims.controller.productmanager to javafx.fxml;
    exports com.hust.ict.aims.controller.productmanager;
    opens com.hust.ict.aims.service to javafx.fxml;
    exports com.hust.ict.aims.service;
    opens com.hust.ict.aims.controller.admin to javafx.fxml;
    exports com.hust.ict.aims.controller.admin;

	requires javafx.base;
	requires org.mockito;
    requires org.simplejavamail.core;
    requires org.checkerframework.checker.qual;
    opens com.hust.ict.aims.entity.media to javafx.base;
    opens com.hust.ict.aims.entity.user to javafx.base;

    exports com.hust.ict.aims;
}