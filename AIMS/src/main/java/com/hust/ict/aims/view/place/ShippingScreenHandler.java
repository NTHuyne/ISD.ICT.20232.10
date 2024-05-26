package com.hust.ict.aims.view.place;

import com.hust.ict.aims.controller.PlaceOrderController;
import com.hust.ict.aims.entity.invoice.Invoice;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.utils.ErrorAlert;
import com.hust.ict.aims.view.BaseScreenHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import com.hust.ict.aims.entity.shipping.DeliveryInfo;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ShippingScreenHandler extends BaseScreenHandler {

    @FXML
    private ImageView aimsImage;

    @FXML
    private TextField addressField;

    @FXML
    private TextField instructionsField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private ChoiceBox<String> provinceField;

    @FXML
    private Button submitDeliveryInfoButton;

    @FXML
    private Button placeRushOrderButton;

    public ShippingScreenHandler(Stage stage, String screenPath, PlaceOrderController placeOrderController) throws IOException {
        super(stage, screenPath);
        File file = new File(Configs.IMAGE_PATH + "/Logo.png");
        Image im = new Image(file.toURI().toString());
        aimsImage.setImage(im);

        provinceField.getItems().addAll(Configs.PROVINCES);

        // on mouse clicked, we back to home
        aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });

        submitDeliveryInfoButton.setOnMouseClicked(e -> {
            if(!nameField.getText().isEmpty() && !phoneField.getText().isEmpty() &&
                !addressField.getText().isEmpty() && !provinceField.getValue().isEmpty()) {
                DeliveryInfo deliveryInfo = new DeliveryInfo(nameField.getText(), phoneField.getText(), provinceField.getValue(), addressField.getText(), instructionsField.getText());
                Order order = placeOrderController.createOrder(deliveryInfo);
                // doing
                Invoice invoice = placeOrderController.createInvoice(order);
                try {
                    InvoiceHandler invoiceHandler = new InvoiceHandler(this.stage, Configs.INVOICE_SCREEN_PATH);
                }
                catch(Exception exp) {
                    exp.printStackTrace();
                }
            }
            else {
                ErrorAlert errorAlert = new ErrorAlert();
                errorAlert.createAlert("Error Message", null, "Some fields were left blank!");
                errorAlert.show();
                throw new RuntimeException("Some field were left blank!");
            }
        });

        placeRushOrderButton.setOnMouseClicked(e -> {
            // place rush order
            placeOrderController.placeRushOrder();
        });
    }

}
