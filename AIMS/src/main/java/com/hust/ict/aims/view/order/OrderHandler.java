package com.hust.ict.aims.view.order;

import java.io.IOException;
import java.util.ArrayList;

import com.hust.ict.aims.controller.ViewOrderController;
import com.hust.ict.aims.persistence.dao.order.OrderDAO;
import com.hust.ict.aims.view.BaseScreenHandler;
import com.hust.ict.aims.view.home.HomeScreenHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
        
        searchButton.setOnAction(event -> {
            String inputOrder = searchFieldOrderid.getText();
            String inputEmail = searchFieldEmail.getText();
            if (!inputOrder.isEmpty() && !inputEmail.isEmpty()) {
            	ArrayList<String> orderInfo = new ArrayList<String>();
            	orderInfo = viewOrderController.getOrderById(Integer.parseInt(inputOrder), inputEmail, new OrderDAO());

            	System.out.println(">>>check order info: " + orderInfo);
                if (orderInfo != null) {
                    // Hiển thị GridPane và fill thông tin
                    gridPane.setVisible(true);
                    noOrderFoundLabel.setVisible(false);
                    fillOrderInformation(orderInfo);
                } else {
                    // Hiển thị thông báo không tìm thấy order
                	noOrderFoundLabel.setVisible(true);
                    gridPane.setVisible(false);
                }
            } else {
            	alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Order ID and Email cannot be left blank");
                alert.showAndWait();
            }
        });
    }
    private void fillOrderInformation(ArrayList<String> orderInfo) {
      System.out.println("thong tin order: " + orderInfo.get(0) +  " " + orderInfo.get(1));
    	orderIdField.setText(orderInfo.get(0));
    	totalField.setText(orderInfo.get(9));
        subtotalField.setText(orderInfo.get(1));
    	statusField.setText(orderInfo.get(2));
    	recipientNameField.setText(orderInfo.get(3));
    	phoneField.setText(orderInfo.get(4));
    	emailField.setText(orderInfo.get(5));
    	addressField.setText(orderInfo.get(6));
    	if(orderInfo.get(7).equals("Khong co") && orderInfo.get(8).equals("Khong co")) {
    		deliveryTimeField.setVisible(false);
    		instructionField.setVisible(false);
    		labelDelivery.setVisible(false);
    		labelInstruction.setVisible(false);
    	} else {
    		deliveryTimeField.setText(orderInfo.get(7));
    		instructionField.setText(orderInfo.get(8));    		
    	}
    	
    	
    }
}