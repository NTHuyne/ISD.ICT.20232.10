package com.hust.ict.aims.view.place;

import com.hust.ict.aims.controller.PlaceOrderController;
import com.hust.ict.aims.entity.invoice.Invoice;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.order.OrderMedia;
import com.hust.ict.aims.entity.shipping.DeliveryInfo;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.utils.ConfirmationAlert;
import com.hust.ict.aims.utils.Utils;
import com.hust.ict.aims.view.BaseScreenHandler;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RushDeliveryInvoiceHandler extends BaseScreenHandler {
    @FXML
    private Label addressField;

    @FXML
    private ImageView aimsImage;

    @FXML
    private VBox coverVBox;

    @FXML
    private Label pageTitle;

    @FXML
    private VBox paymentInfoVBox;

    @FXML
    private Label phoneField;

    @FXML
    private Label recipientNameField;

    @FXML
    private Label regularDeliveryShipFeeLabel;

    @FXML
    private Label regularDeliverySubtotalLabel;

    @FXML
    private VBox regularDeliveryVBox;

    @FXML
    private Label regularDeliveryVatLabel;

    @FXML
    private Label rushDeliveryShipFeeLabel;

    @FXML
    private Label rushDeliverySubtotalLabel;

    @FXML
    private VBox rushDeliveryVBox;

    @FXML
    private Label rushDeliveryVatLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Button payOrderBtn;

    @FXML
    private Button cancelOrderBtn;

    private PlaceOrderController placeOrderController;

    public RushDeliveryInvoiceHandler(Stage stage, String screenPath, Order regularOrder, Order rushOrder, PlaceOrderController placeOrderController) throws IOException {
        super(stage, screenPath);

        String imagePath = "/assets/images/Logo.png";
        Image im = new Image(getClass().getResourceAsStream(imagePath));
        aimsImage.setImage(im);
        this.placeOrderController = placeOrderController;

        DeliveryInfo deliveryInfo = regularOrder.getDeliveryInfo();

        setUpData(regularOrder, rushOrder, deliveryInfo);

        // on mouse clicked, we back to home
        aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });

        payOrderBtn.setOnMouseClicked(e -> {
            requestPayOrder();
        });

        cancelOrderBtn.setOnMouseClicked(e -> {
            // Go back to home screen
            ConfirmationAlert confirmationAlert = new ConfirmationAlert();
            confirmationAlert.createAlert("Error message: ", null, "Are you sure to cancel the order?");
            confirmationAlert.show();
            if(confirmationAlert.isConfirmed())
                homeScreenHandler.show();
        });
    }

    public void setUpData(Order regularOrder, Order rushOrder, DeliveryInfo deliveryInfo) {
        recipientNameField.setText(deliveryInfo.getName());
        phoneField.setText(deliveryInfo.getPhone());
        addressField.setText(deliveryInfo.getAddress() + ", " + deliveryInfo.getProvince());

        if(!regularOrder.getLstOrderMedia().isEmpty()) displayItems(regularOrder);
        displayItems(rushOrder);

        addPaymentOptions();

        // Display invoice for regular order
        int regSubtotal = 0;
        if(regularOrder.getLstOrderMedia().isEmpty()) regularDeliveryVBox.setVisible(false);
        else {
            regSubtotal = placeOrderController.calculateSubTotal(regularOrder).getSubtotal();
            regularDeliverySubtotalLabel.setText(
                    Utils.getCurrencyFormat(regSubtotal)
            );
            regularDeliveryVatLabel.setText(Utils.getCurrencyFormat(regSubtotal / 10));
            regularDeliveryShipFeeLabel.setText(
                    Utils.getCurrencyFormat(placeOrderController.calculateShippingFee(regularOrder))
            );
        }

        // Display invoice for rush order
        int rushSubtotal = placeOrderController.calculateSubTotal(rushOrder).getSubtotal();
        rushDeliverySubtotalLabel.setText(
                Utils.getCurrencyFormat(rushSubtotal)
        );
        rushDeliveryVatLabel.setText(Utils.getCurrencyFormat(rushSubtotal / 10));
        rushDeliveryShipFeeLabel.setText(
                Utils.getCurrencyFormat(placeOrderController.calculateShippingFee(rushOrder))
        );

        priceLabel.setText(
                Utils.getCurrencyFormat(
                        regSubtotal * 11 / 10 + rushSubtotal * 11 / 10
                            + placeOrderController.calculateShippingFee(regularOrder)
                            + placeOrderController.calculateShippingFee(rushOrder)
                )
        );
    }

    public void displayItems(Order order) {
        Label orderLabel = new Label(); orderLabel.setFont(new Font(24));
        if(order.getIsRushOrder()) orderLabel.setText("Rush Delivery Items:");
        else orderLabel.setText("Regular Delivery Items");
        coverVBox.getChildren().add(orderLabel);

        Pane emptyPane = new Pane(); emptyPane.setPrefWidth(70); emptyPane.setPrefHeight(100);
        Label lbl1 = new Label("Media"); lbl1.setFont(new Font(18)); lbl1.setPrefWidth(317); lbl1.setPrefHeight(23);
        Separator sep = new Separator(Orientation.VERTICAL); sep.setPrefHeight(200);
        Label lbl2 = new Label("Unit price"); lbl2.setFont(new Font(18)); lbl2.setPrefWidth(190); lbl2.setPrefHeight(23);
        Label lbl3 = new Label("Quantity"); lbl3.setFont(new Font(18)); lbl3.setPrefWidth(174); lbl3.setPrefHeight(23);
        Label lbl4 = new Label("Total"); lbl4.setFont(new Font(18)); lbl4.setPrefWidth(354); lbl4.setPrefHeight(23);
        HBox headHBox = new HBox(); headHBox.setStyle("-fx-background-color: fff; -fx-background-radius: 20;"); headHBox.setAlignment(Pos.CENTER_LEFT);
            headHBox.setPrefHeight(46); headHBox.setPrefWidth(1139);
        headHBox.getChildren().add(emptyPane); headHBox.getChildren().add(lbl1); headHBox.getChildren().add(new Separator(Orientation.VERTICAL));
        headHBox.getChildren().add(lbl2); headHBox.getChildren().add(new Separator(Orientation.VERTICAL)); headHBox.getChildren().add(lbl3);
        headHBox.getChildren().add(new Separator(Orientation.VERTICAL)); headHBox.getChildren().add(lbl4);
        coverVBox.getChildren().add(headHBox);

        ScrollPane scrollPane = new ScrollPane(); scrollPane.setPrefHeight(100); scrollPane.setPrefWidth(200);
        VBox itemsVBox = new VBox(); itemsVBox.setPrefHeight(90); itemsVBox.setPrefWidth(900);
        try{
            for(Object om : order.getLstOrderMedia()) {
                OrderMedia orderMedia = (OrderMedia) om;
                MediaHandler mediaInvoiceScreen = new MediaHandler(Configs.INVOICE_MEDIA_PATH, orderMedia);
                itemsVBox.getChildren().add(mediaInvoiceScreen.getContent());
                itemsVBox.getChildren().add(new Separator());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        scrollPane.setContent(itemsVBox);
        coverVBox.getChildren().add(scrollPane);
    }

    public void addPaymentOptions() {
        // Add payment options
        Separator sep = new Separator();
        sep.setHalignment(HPos.CENTER);
        sep.setValignment(VPos.CENTER);
        sep.setPadding(new Insets(0,0,0,50));
        Label label = new Label();
        label.setText("Choose payment method");
        label.setFont(new Font(20));
        HBox hbox = new HBox();
        RadioButton radioBtn = new RadioButton("VNPay");
        radioBtn.setFont(new Font(18));
        hbox.getChildren().add(radioBtn);
        coverVBox.getChildren().add(sep);
        coverVBox.getChildren().add(label);
        coverVBox.getChildren().add(hbox);
    }

    public void requestPayOrder() {

    }
}
