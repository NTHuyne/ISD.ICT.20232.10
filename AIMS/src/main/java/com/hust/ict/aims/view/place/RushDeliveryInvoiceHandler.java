package com.hust.ict.aims.view.place;

import java.io.IOException;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import com.hust.ict.aims.controller.PlaceOrderController;
import com.hust.ict.aims.entity.invoice.Invoice;
import com.hust.ict.aims.entity.order.OrderMedia;
import com.hust.ict.aims.entity.order.RushOrder;
import com.hust.ict.aims.entity.payment.PaymentTransaction;
import com.hust.ict.aims.entity.shipping.DeliveryInfo;
import com.hust.ict.aims.exception.PaymentException;
import com.hust.ict.aims.persistence.dao.media.MediaDAO;
import com.hust.ict.aims.persistence.dao.order.OrderDAO;
import com.hust.ict.aims.persistence.dao.payment.InvoiceDAO;
import com.hust.ict.aims.subsystem.payment.IClient;
import com.hust.ict.aims.subsystem.payment.vnpay.VNPayOrderManager;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.utils.ConfirmationAlert;
import com.hust.ict.aims.utils.ErrorAlert;
import com.hust.ict.aims.utils.InformationAlert;
import com.hust.ict.aims.utils.Utils;
import com.hust.ict.aims.view.BaseScreenHandler;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class RushDeliveryInvoiceHandler extends BaseScreenHandler implements IClient {
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
    private Label emailField;

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

    
    private Invoice invoice;
    public RushDeliveryInvoiceHandler(Stage stage, String screenPath, RushOrder order, PlaceOrderController placeOrderController) throws IOException {
        super(stage, screenPath);

        this.invoice = placeOrderController.createInvoice(order);
        String imagePath = "/assets/images/Logo.png";
        Image im = new Image(getClass().getResourceAsStream(imagePath));
        aimsImage.setImage(im);
        this.placeOrderController = placeOrderController;

        DeliveryInfo deliveryInfo = order.getDeliveryInfo();

        setUpData(order, deliveryInfo);

        // on mouse clicked, we back to home
        aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });

        payOrderBtn.setOnMouseClicked(e -> {
        	// 
            InformationAlert infoAlert = new InformationAlert();
            infoAlert.createAlert("Notification", null, "Your rush delivery will arrive at " + order.getDeliveryTimeAsString() );
            
            infoAlert.show();
            requestPayOrder();
        });

        cancelOrderBtn.setOnMouseClicked(e -> {
            // Go back to home screen
            ConfirmationAlert confirmationAlert = new ConfirmationAlert();
            confirmationAlert.createAlert("Error message: ", null, "Are you sure to cancel the order?");
            confirmationAlert.show();
            if(confirmationAlert.isConfirmed()) {
				homeScreenHandler.show();
			}
        });
    }

    private int totalAllFee;
    public void setUpData(RushOrder order, DeliveryInfo deliveryInfo) {
        recipientNameField.setText(deliveryInfo.getName());
        phoneField.setText(deliveryInfo.getPhone());
        addressField.setText(deliveryInfo.getAddress() + ", " + deliveryInfo.getProvince());
        emailField.setText(deliveryInfo.getEmail());

        List<OrderMedia> regularOrder = new ArrayList<>();
        List<OrderMedia> rushOrder = new ArrayList<>();

        for(OrderMedia om : order.getLstOrderMedia()) {
            if(om.getOrderType() == OrderMedia.OrderType.NORMAL) regularOrder.add(om);
            else rushOrder.add(om);
        }

        if(!regularOrder.isEmpty()) {
            Label orderLabel = new Label(); orderLabel.setFont(new Font(24));
            orderLabel.setText("Regular Delivery Items:");
            coverVBox.getChildren().add(orderLabel);
			displayItems(regularOrder, false);
		}
        Label orderLabel = new Label(); orderLabel.setFont(new Font(24));
        orderLabel.setText("Rush Delivery Items:");
        coverVBox.getChildren().add(orderLabel);
        displayItems(rushOrder, true);

//        addPaymentOptions();

        // Display invoice for regular order
        int regSubtotal = 0;
        if(regularOrder.isEmpty()) {
			regularDeliveryVBox.setVisible(false);
		} else {
            regSubtotal = placeOrderController.calculateSubTotal(regularOrder);
            regularDeliverySubtotalLabel.setText(
                    Utils.getCurrencyFormat(regSubtotal)
            );
            regularDeliveryVatLabel.setText(Utils.getCurrencyFormat(regSubtotal / 10));
            regularDeliveryShipFeeLabel.setText(
                    Utils.getCurrencyFormat(placeOrderController.calculateShippingFee(regularOrder,
                            OrderMedia.OrderType.NORMAL, order.getDeliveryInfo().getProvince()))
            );
        }

        // Display invoice for rush order
        int rushSubtotal = placeOrderController.calculateSubTotal(rushOrder);
        rushDeliverySubtotalLabel.setText(
                Utils.getCurrencyFormat(rushSubtotal)
        );
        rushDeliveryVatLabel.setText(Utils.getCurrencyFormat(rushSubtotal / 10));
        rushDeliveryShipFeeLabel.setText(
                Utils.getCurrencyFormat(placeOrderController.calculateShippingFee(rushOrder,
                        OrderMedia.OrderType.RUSH, order.getDeliveryInfo().getProvince()))
        );
        order.setShippingFees(
                placeOrderController.calculateShippingFee(regularOrder,
                        OrderMedia.OrderType.NORMAL, order.getDeliveryInfo().getProvince()) +
                placeOrderController.calculateShippingFee(rushOrder,
                        OrderMedia.OrderType.RUSH, order.getDeliveryInfo().getProvince())
        );
        order.setSubtotal(regSubtotal * 11 / 10 + rushSubtotal * 11 / 10
                + order.getShippingFees() );
        
        totalAllFee = order.getSubtotal();
        
        priceLabel.setText(Utils.getCurrencyFormat(totalAllFee)
        );
    }

    public void displayItems(List<OrderMedia> order, boolean isRushOrderMedia) {
        Pane emptyPane = new Pane(); emptyPane.setPrefWidth(70); emptyPane.setPrefHeight(100);
        Label lbl1 = new Label("Media"); lbl1.setFont(new Font(18)); lbl1.setPrefWidth(350); lbl1.setPrefHeight(23);
//        Separator sep = new Separator(Orientation.VERTICAL); sep.setPrefHeight(200);
        Label lbl2 = new Label("Unit price"); lbl2.setFont(new Font(18)); lbl2.setPrefWidth(200); lbl2.setPrefHeight(23);
        Label lbl3 = new Label("Quantity"); lbl3.setFont(new Font(18)); lbl3.setPrefWidth(180); lbl3.setPrefHeight(23);
        Label lbl4 = new Label("Total"); lbl4.setFont(new Font(18)); lbl4.setPrefWidth(354); lbl4.setPrefHeight(23);
        HBox headHBox = new HBox(); headHBox.setStyle("-fx-background-color: fff; -fx-background-radius: 20;"); headHBox.setAlignment(Pos.CENTER_LEFT);
            headHBox.setPrefHeight(46); headHBox.setPrefWidth(1139);
        headHBox.getChildren().add(emptyPane);
        headHBox.getChildren().add(lbl1);
//        headHBox.getChildren().add(new Separator(Orientation.VERTICAL));
        headHBox.getChildren().add(lbl2);
//        headHBox.getChildren().add(new Separator(Orientation.VERTICAL));
        headHBox.getChildren().add(lbl3);
//        headHBox.getChildren().add(new Separator(Orientation.VERTICAL));
        headHBox.getChildren().add(lbl4);
        coverVBox.getChildren().add(headHBox);

        ScrollPane scrollPane = new ScrollPane(); scrollPane.setPrefHeight(100); scrollPane.setPrefWidth(200);
        VBox itemsVBox = new VBox(); itemsVBox.setPrefHeight(90); itemsVBox.setPrefWidth(900);
        try{
            for(Object om : order) {
                OrderMedia orderMedia = (OrderMedia) om;
                OrderMedia.OrderType orderType = (isRushOrderMedia ? OrderMedia.OrderType.RUSH : OrderMedia.OrderType.NORMAL);
                if(((OrderMedia) om).getOrderType() == orderType) {
                    MediaHandler mediaInvoiceScreen = new MediaHandler(Configs.INVOICE_MEDIA_PATH, orderMedia);
                    itemsVBox.getChildren().add(mediaInvoiceScreen.getContent());
                    itemsVBox.getChildren().add(new Separator());
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        scrollPane.setContent(itemsVBox);
        coverVBox.getChildren().add(scrollPane);
    }

//    public void addPaymentOptions() {
//        // Add payment options
//        Separator sep = new Separator();
//        sep.setHalignment(HPos.CENTER);
//        sep.setValignment(VPos.CENTER);
//        sep.setPadding(new Insets(0,0,0,50));
//        Label label = new Label();
//        label.setText("Choose payment method");
//        label.setFont(new Font(20));
//        HBox hbox = new HBox();
//        RadioButton radioBtn = new RadioButton("VNPay");
//        radioBtn.setFont(new Font(18));
//        hbox.getChildren().add(radioBtn);
//        coverVBox.getChildren().add(sep);
//        coverVBox.getChildren().add(label);
//        coverVBox.getChildren().add(hbox);
//    }
    

    @FXML
    private AnchorPane loadingOverlay;

    // VNPay Stuffs
    private static VNPayOrderManager vnpayManager;
    public void requestPayOrder() {
    	// ProgressIndicator pg;
    	loadingOverlay.setVisible(true);
    	if (vnpayManager == null) {
    		System.out.println("VNPayOrderManager is running new.");
    		vnpayManager = new VNPayOrderManager();
    	}
    	
    	vnpayManager.payOrder(
    		totalAllFee,
			"Payment for " + invoice.getOrder().getDeliveryInfo().getName() + ", " 
				+ invoice.getOrder().getLstOrderMedia().size() + " items",
			this
    	);
    }
    
	@Override
	public void updateTransactionOnFailure(PaymentException exception) {
		loadingOverlay.setVisible(false);
		System.out.println("Transaction Failure!");
		
		Platform.runLater(() -> {
			ErrorAlert failAlert = new ErrorAlert();
			failAlert.createAlert("Transaction Failed", null, "Transaction failed.\nReason: " + exception.getMessage());
			failAlert.show();
		});
	}

	@Override
	public void updateTransactionOnSuccess(PaymentTransaction trans) {
		loadingOverlay.setVisible(false);
		System.out.println("Transaction Success!!!");
		
		this.invoice.setTransaction(trans);
		try {
			new InvoiceDAO(new OrderDAO(new MediaDAO().getAllMedia())).addFromStart(this.invoice);
            placeOrderController.sendSuccessfulOrderMail(invoice);
			
			// Go back to main javafx thread
			Platform.runLater(() -> {
		        InformationAlert successAlert = new InformationAlert();
		        successAlert.createAlert("Transaction Completed", null, "Transaction completed successfully. Awating for product manager to confirm your order.");
		        successAlert.show();
		        homeScreenHandler.show();
			});
		} catch (SQLException e) {
			e.printStackTrace();
			Platform.runLater(() -> {
				ErrorAlert failAlert = new ErrorAlert();
				failAlert.createAlert("Transaction Failed", null, "Transaction failed.\nReason: Cannot insert to database?");
				failAlert.show();
			});
		}
	}
}
