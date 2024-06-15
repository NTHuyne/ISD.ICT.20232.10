package com.hust.ict.aims.view.order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hust.ict.aims.controller.ViewOrderController;
import com.hust.ict.aims.persistence.dao.order.OrderDAO;
import com.hust.ict.aims.view.BaseScreenHandler;
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
    private TableView<List<String>> tableOrderMedia;

    @FXML
    private TableColumn<ArrayList<String>, String> col_id;

    @FXML
    private TableColumn<ArrayList<String>, String> col_title;

    @FXML
    private TableColumn<ArrayList<String>, String> col_unit_price;

    @FXML
    private TableColumn<ArrayList<String>, String> col_quantity;

    @FXML
    private TableColumn<ArrayList<String>, String> col_price;

    @FXML
    private Button cancelOrderBtn;
    
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
            	ArrayList<String> orderInfo = new ArrayList<String>();
                List<ArrayList<String>> mediaInOrder;
            	orderInfo = viewOrderController.getOrderById(Integer.parseInt(inputOrder), inputEmail, new OrderDAO());

            	System.out.println(">>>check order info: " + orderInfo);
                if (orderInfo != null) {
                    // Hiển thị GridPane và fill thông tin
                    gridPane.setVisible(true);
                    noOrderFoundLabel.setVisible(false);
                    tableOrderMedia.setVisible(true);
                    cancelOrderBtn.setVisible(true);
                    mediaInOrder = viewOrderController.getMediaInOrder(Integer.parseInt(inputOrder), new OrderDAO());
                    fillMediaInOrder(mediaInOrder);
                    fillOrderInformation(orderInfo);
                } else {
                    // Hiển thị thông báo không tìm thấy order
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
    private void fillOrderInformation(ArrayList<String> orderInfo) {
      System.out.println("thong tin order: " + orderInfo.get(0) +  " " + orderInfo.get(1));
    	orderIdField.setText(orderInfo.get(0));
    	subtotalField.setText(orderInfo.get(1));
    	statusField.setText(orderInfo.get(2));
    	recipientNameField.setText(orderInfo.get(3));
    	phoneField.setText(orderInfo.get(4));
    	emailField.setText(orderInfo.get(5));
    	addressField.setText(orderInfo.get(6));
    	deliveryTimeField.setText(orderInfo.get(7));
    	instructionField.setText(orderInfo.get(8));
        totalField.setText(orderInfo.get(9));
    	
    }

    private void fillMediaInOrder(List<ArrayList<String>> mediaInOrder){
        col_id.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(tableOrderMedia.getItems().indexOf(cellData.getValue()) + 1)));
        col_title.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        col_unit_price.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        col_quantity.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        col_price.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));

        ObservableList<List<String>> observableData = FXCollections.observableArrayList(mediaInOrder);
        tableOrderMedia.setItems(observableData);
    }
}