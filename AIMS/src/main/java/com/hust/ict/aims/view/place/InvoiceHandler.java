package com.hust.ict.aims.view.place;

import com.hust.ict.aims.controller.PlaceOrderController;
import com.hust.ict.aims.entity.invoice.Invoice;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.order.OrderMedia;
import com.hust.ict.aims.entity.shipping.DeliveryInfo;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.utils.Utils;
import com.hust.ict.aims.view.BaseScreenHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class InvoiceHandler extends BaseScreenHandler {

    @FXML
    private ImageView aimsImage;

    @FXML
    private Button cancelOrderButton;

    @FXML
    private VBox coverVBox;

    @FXML
    private Label pageTitle;

    @FXML
    private Button payOrderButton;

    @FXML
    private Label recipientNameField;

    @FXML
    private Label phoneField;

    @FXML
    private Label addressField;

    @FXML
    private Label subtotalLabel;

    @FXML
    private Label vatLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label shippingFeeLabel;

    private Invoice invoice;

    public InvoiceHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
        super(stage, screenPath);
        this.invoice = invoice;
        File file = new File(Configs.IMAGE_PATH + "/Logo.png");
        Image im = new Image(file.toURI().toString());
        aimsImage.setImage(im);

        DeliveryInfo deliveryInfo = invoice.getOrder().getDeliveryInfo();

        setUpData(invoice.getOrder().getLstOrderMedia(), deliveryInfo);

        // on mouse clicked, we back to home
        aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });

        payOrderButton.setOnMouseClicked(e -> {
            requestPayOrder();
        });

        cancelOrderButton.setOnMouseClicked(e -> {
            // Go back to cart screen

        });


    }

    public void setUpData(List<OrderMedia> lstOrderMedia, DeliveryInfo deliveryInfo) {
        recipientNameField.setText(deliveryInfo.getName());
        phoneField.setText(deliveryInfo.getPhone());
        addressField.setText(deliveryInfo.getAddress() + ", " + deliveryInfo.getProvince());

        subtotalLabel.setText(Utils.getCurrencyFormat(this.invoice.getTotalAmount()));
        vatLabel.setText(Utils.getCurrencyFormat(this.invoice.getTotalAmount() * 8 / 100));
        priceLabel.setText(Utils.getCurrencyFormat(
                this.invoice.getTotalAmount() * 108 / 100 + 25000
        ));

        try{
            for(Object om : lstOrderMedia) {
                OrderMedia orderMedia = (OrderMedia) om;
                MediaHandler mediaInvoiceScreen = new MediaHandler(Configs.INVOICE_MEDIA_PATH, orderMedia);
                coverVBox.getChildren().add(mediaInvoiceScreen.getContent());
            }
            addPaymentOptions();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
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
