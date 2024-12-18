package com.hust.ict.aims.view.place;

import java.io.IOException;

import com.hust.ict.aims.controller.PlaceOrderController;
import com.hust.ict.aims.entity.invoice.Invoice;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.shipping.DeliveryInfo;
import com.hust.ict.aims.exception.placement.RushOrderUnsupportedException;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.utils.ErrorAlert;
import com.hust.ict.aims.view.BaseScreenHandler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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
    private TextField emailField;

    @FXML
    private ChoiceBox<String> provinceField;

    @FXML
    private Button submitDeliveryInfoButton;

    @FXML
    private Button placeRushOrderButton;

    public ShippingScreenHandler(Stage stage, String screenPath, PlaceOrderController placeOrderController) throws IOException {
        super(stage, screenPath);
        String imagePath = "/assets/images/Logo.png";
        Image im = new Image(getClass().getResourceAsStream(imagePath));
        aimsImage.setImage(im);

        provinceField.getItems().addAll(Configs.PROVINCES);

        // on mouse clicked, we back to home
        aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });

        submitDeliveryInfoButton.setOnMouseClicked(e -> {
            if(!nameField.getText().isEmpty() && PlaceOrderController.validatePhoneField(phoneField) &&
                !addressField.getText().isEmpty() && !provinceField.getValue().isEmpty()
                && PlaceOrderController.validateEmailField(emailField)) {
                DeliveryInfo deliveryInfo = new DeliveryInfo(nameField.getText(), phoneField.getText(), provinceField.getValue(), addressField.getText(), emailField.getText(), instructionsField.getText()) ;
                Order order = placeOrderController.createOrder(deliveryInfo);
                Invoice invoice = placeOrderController.createInvoice(order);

                try {
                    InvoiceHandler invoiceHandler = new InvoiceHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice, placeOrderController);
                    invoiceHandler.setHomeScreenHandler(homeScreenHandler);
                    invoiceHandler.setScreenTitle("Invoice");
                    invoiceHandler.show();
                }
                catch(Exception exp) {
                    exp.printStackTrace();
                }
            }
            else {
                if(!PlaceOrderController.validatePhoneField(phoneField)) {
                    ErrorAlert errorAlert = new ErrorAlert();
                    errorAlert.createAlert("Error Message", null, "Invalid phone number!");
                    errorAlert.show();
                    throw new RuntimeException("Some field were left blank! Please re-enter delivery information");
                }
                if(!PlaceOrderController.validateEmailField(emailField)) {
                    ErrorAlert errorAlert = new ErrorAlert();
                    errorAlert.createAlert("Error Message", null, "Invalid email!");
                    errorAlert.show();
                    throw new RuntimeException("Some field were left blank! Please re-enter delivery information");
                }
                ErrorAlert errorAlert = new ErrorAlert();
                errorAlert.createAlert("Error Message", null, "Some fields were left blank!");
                errorAlert.show();
                throw new RuntimeException("Some field were left blank! Please re-enter delivery information");
            }
        });

        placeRushOrderButton.setOnMouseClicked(e -> {
            // place rush order
            if(!nameField.getText().isEmpty() && PlaceOrderController.validatePhoneField(phoneField) &&
                    !addressField.getText().isEmpty() && !provinceField.getValue().isEmpty()
                    && PlaceOrderController.validateEmailField(emailField)) {
                DeliveryInfo deliveryInfo = new DeliveryInfo(nameField.getText(), phoneField.getText(), provinceField.getValue(), addressField.getText(), emailField.getText(), instructionsField.getText());
                Order order = placeOrderController.createOrder(deliveryInfo);
                Invoice invoice = placeOrderController.createInvoice(order);
                try {
                    boolean canPlaceRushOrder = placeOrderController.canPlaceRushOrder(order);
                    if(!canPlaceRushOrder) {
                        throw new RushOrderUnsupportedException();
                    }
                    RushDeliveryShippingScreenHandler rushDeliveryShippingScreen = new RushDeliveryShippingScreenHandler(this.stage, Configs.RUSH_ORDER_SHIPPING_SCREEN_PATH, placeOrderController, invoice);
                    rushDeliveryShippingScreen.setPreviousScreen(this);
                    rushDeliveryShippingScreen.setHomeScreenHandler(homeScreenHandler);
                    rushDeliveryShippingScreen.setScreenTitle("Rush Delivery Shipping Form");
                    rushDeliveryShippingScreen.setBController(placeOrderController);
                    rushDeliveryShippingScreen.show();
                }
                catch(RushOrderUnsupportedException exp){
                    ErrorAlert errorAlert = new ErrorAlert();
                    errorAlert.createAlert("Error message", null, "Rush delivery service doesn\'t support current address or any items in the order!");
                    errorAlert.show();
                    throw new RuntimeException("Rush order unsupported");
                }
                catch(Exception e1) {
                    e1.printStackTrace();
                }
            }
            else {
                if(!PlaceOrderController.validatePhoneField(phoneField)) {
                    ErrorAlert errorAlert = new ErrorAlert();
                    errorAlert.createAlert("Error Message", null, "Invalid phone number!");
                    errorAlert.show();
                    throw new RuntimeException("Some field were left blank! Please re-enter delivery information");
                }
                if(!PlaceOrderController.validateEmailField(emailField)) {
                    ErrorAlert errorAlert = new ErrorAlert();
                    errorAlert.createAlert("Error Message", null, "Invalid email!");
                    errorAlert.show();
                    throw new RuntimeException("Some field were left blank! Please re-enter delivery information");
                }
                ErrorAlert errorAlert = new ErrorAlert();
                errorAlert.createAlert("Error Message", null, "Some fields were left blank!");
                errorAlert.show();
                throw new RuntimeException("Some field were left blank! Please re-enter delivery information");
            }
        });
    }


}
