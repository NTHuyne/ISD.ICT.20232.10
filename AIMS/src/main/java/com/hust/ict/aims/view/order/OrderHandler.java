package com.hust.ict.aims.view.order;

import java.io.IOException;
import java.util.List;

import com.hust.ict.aims.controller.ViewOrderController;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.order.OrderMedia;
import com.hust.ict.aims.entity.shipping.DeliveryInfo;
import com.hust.ict.aims.view.BaseScreenHandler;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class OrderHandler extends BaseScreenHandler{
	
	@FXML
    private ImageView aimsImage;

	@FXML
    private TextField searchFieldOrderid;
	
	@FXML
    private TextField searchFieldEmail;

    @FXML
    private Button searchButton;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label noOrderFoundLabel;

    @FXML
    private Label orderIdField;

    @FXML
    private Label recipientNameField;
    
    @FXML
    private Label phoneField;
    
    @FXML
    private Label emailField;
    
    @FXML
    private Label addressField;
    
    @FXML
    private Label subtotalField;

    @FXML
    private Label totalField;
    
    @FXML
    private Label deliveryTimeField;    
    
    @FXML
    private Label instructionField;
    
    @FXML
    private Label statusField;

    @FXML
    private TableView<OrderMedia> tableOrderMedia;

    @FXML
    private TableColumn<OrderMedia, Integer> col_id;

    @FXML
    private TableColumn<OrderMedia, String> col_title;

    @FXML
    private TableColumn<OrderMedia, Integer> col_unit_price;

    @FXML
    private TableColumn<OrderMedia, Integer> col_quantity;

    @FXML
    private TableColumn<OrderMedia, Integer> col_price;

    @FXML
    private Button cancelOrderBtn;

    @FXML
    private Label labelDelivery;

    @FXML
    private Label labelInstruction;
    
    public Alert alert;
    
    ClassLoader classLoader = getClass().getClassLoader();
    
    private ViewOrderController viewOrderController = new ViewOrderController();
    
    public OrderHandler(Stage stage, String screenPath) throws IOException {
    	super(stage, screenPath);
    	// TODO Auto-generated constructor stub
    	aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });
    }
    @FXML
    private void initialize() {
        gridPane.setVisible(false);
        tableOrderMedia.setVisible(false);
        cancelOrderBtn.setVisible(false);
        
        searchButton.setOnAction(event -> {
            String inputOrder = searchFieldOrderid.getText();
            String inputEmail = searchFieldEmail.getText();
            if (!inputOrder.isEmpty() && !inputEmail.isEmpty()) {
            	
            	List<OrderMedia> mediaInOrder;
            	try {
                	Order orderInfo = viewOrderController.getOrderById(Integer.parseInt(inputOrder), inputEmail);
                    // Hiển thị GridPane và fill thông tin
                    gridPane.setVisible(true);
                    noOrderFoundLabel.setVisible(false);
                    tableOrderMedia.setVisible(true);
                    cancelOrderBtn.setVisible(true);
                    mediaInOrder = orderInfo.getLstOrderMedia();
                    
                    fillMediaInOrder(mediaInOrder);
                    fillOrderInformation(orderInfo);
            	} catch(Exception e) {
                  // Hiển thị thông báo không tìm thấy order
            		System.err.println(e.getMessage());
					noOrderFoundLabel.setVisible(true);
					gridPane.setVisible(false);
					tableOrderMedia.setVisible(false);
					cancelOrderBtn.setVisible(false);
            	}

            } else {
            	alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("orderid and email cannot be left blank");
                alert.showAndWait();
            }
        });
    }
    private void fillOrderInformation(Order orderInfo) {
    	System.out.println("thong tin order: " + orderInfo.getId());
		orderIdField.setText(String.valueOf(orderInfo.getId()));
		subtotalField.setText(String.valueOf(orderInfo.getSubtotal()));
		statusField.setText(orderInfo.getStatus().toString());
		totalField.setText(String.valueOf((int) (orderInfo.getSubtotal()*1.1) + orderInfo.getShippingFees()));
		DeliveryInfo delivery = orderInfo.getDeliveryInfo();
		recipientNameField.setText(delivery.getName());
		phoneField.setText(delivery.getPhone());
		emailField.setText(delivery.getEmail());
		addressField.setText(delivery.getAddress());

		// TODO: FIX THIS with rushorder
//        if(orderInfo.get(7).equals("Khong co") && orderInfo.get(8).equals("Khong co")) {
//            deliveryTimeField.setVisible(false);
//            instructionField.setVisible(false);
//            labelDelivery.setVisible(false);
//            labelInstruction.setVisible(false);
//        } else {
//            deliveryTimeField.setText(orderInfo.get(7));
//            instructionField.setText(orderInfo.get(8));
//        }
		// TODO: FIX THIS TOO
        // totalField.setText(orderInfo.getT);
    	
    }

    private void fillMediaInOrder(List<OrderMedia> mediaInOrder){
        col_id.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMedia().getMediaId()).asObject());
        col_title.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMedia().getTitle()));
        col_unit_price.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMedia().getMediaId()).asObject());
        col_quantity.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMedia().getMediaId()).asObject());
        col_price.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMedia().getMediaId()).asObject());

        ObservableList<OrderMedia> observableData = FXCollections.observableArrayList(mediaInOrder);
        tableOrderMedia.setItems(observableData);
    }
}