package com.hust.ict.aims.controller.productmanager;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.order.OrderMedia;
import com.hust.ict.aims.entity.shipping.DeliveryInfo;
import com.hust.ict.aims.persistence.dao.media.MediaDAO;
import com.hust.ict.aims.persistence.dao.order.OrderDAO;
import com.hust.ict.aims.subsystem.email.IEmail;
import com.hust.ict.aims.subsystem.email.simplejavamail.SimplejavamailManager;
import com.hust.ict.aims.utils.ErrorAlert;
import com.hust.ict.aims.utils.InformationAlert;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ProductManagerOrderController implements Initializable, DataChangedListener {

	private OrderDAO orderDAO;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			this.orderDAO = new OrderDAO(new MediaDAO().getAllMedia());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.orderShowData();
	}

	@Override
	public void onDataChanged() {
		this.orderShowData();
	}

    @FXML
    private Label delivery_address;

    @FXML
    private Label delivery_email;

    @FXML
    private Label delivery_instruction;

    @FXML
    private Label delivery_name;

    @FXML
    private Label delivery_phone;

    @FXML
    private Label delivery_province;

    @FXML
    private AnchorPane main_form;

    @FXML
    private TableColumn<OrderMedia, Integer> omedias_col_id;

    @FXML
    private TableColumn<OrderMedia, String> omedias_col_orderType;

    @FXML
    private TableColumn<OrderMedia, Integer> omedias_col_quantity;

    @FXML
    private TableColumn<OrderMedia, String> omedias_col_title;

    @FXML
    private TableView<OrderMedia> omedias_tableView;

    @FXML
    private TableColumn<Order, Integer> orders_col_id;
    
    @FXML
    private TableColumn<Order, Integer> orders_col_subtotal;

    @FXML
    private TableColumn<Order, Integer> orders_col_shippingFee;

    @FXML
    private TableColumn<Order, String> orders_col_status;

    @FXML
    private TableColumn<Order, String> orders_col_type;
    
    @FXML
    private TableView<Order> orders_tableView;

    @FXML
    void acceptOrder(ActionEvent event) {
    	try {
			this.orderDAO.acceptOrder(selectedOrder);
			
			this.sendStateOrderMail(selectedOrder);
		} catch (SQLException e) {
			e.printStackTrace();
            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Error Message", null, e.getMessage());
            errorAlert.show();
		}
    	orders_tableView.refresh();
    }


    @FXML
    void rejectOrder(ActionEvent event) {
    	try {
			this.orderDAO.rejectOrder(selectedOrder);
			
			this.sendStateOrderMail(selectedOrder);
		} catch (SQLException e) {
			e.printStackTrace();
            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Error Message", null, e.getMessage());
            errorAlert.show();
		}
    	orders_tableView.refresh();
    }
    
	private ObservableList<Order> orderListData;

	private void orderShowData() {
        try {
        	orderListData = FXCollections.observableArrayList(orderDAO.getAll());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	    orders_tableView.setItems(orderListData);
        
	    orders_col_id.setCellValueFactory(col -> new SimpleIntegerProperty(col.getValue().getId()).asObject());
	    orders_col_subtotal.setCellValueFactory(col -> new SimpleIntegerProperty(col.getValue().getSubtotal()).asObject());
	    orders_col_shippingFee.setCellValueFactory(col -> new SimpleIntegerProperty(col.getValue().getShippingFees()).asObject());
	    orders_col_status.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getStatus().toString()));
	    orders_col_type.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getTypeName()));
	}
	
	private void orderMediaShowData() {
		omedias_tableView.setItems(FXCollections.observableArrayList(selectedOrder.getLstOrderMedia()));
		
		omedias_col_id.setCellValueFactory(col -> new SimpleIntegerProperty(col.getValue().getMedia().getMediaId()).asObject());
		omedias_col_quantity.setCellValueFactory(col -> new SimpleIntegerProperty(col.getValue().getQuantity()).asObject());
		omedias_col_title.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getMedia().getTitle()));
		omedias_col_orderType.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOrderType().toString()));
	}

    private Order selectedOrder;
    
    @FXML
    void selectOrder(MouseEvent event) {
    	selectedOrder = orders_tableView.getSelectionModel().getSelectedItem();
    	
    	if (selectedOrder == null) return;
    	
    	orderMediaShowData();

    	DeliveryInfo currentDelivery = selectedOrder.getDeliveryInfo();
    	
        delivery_address.setText(currentDelivery.getAddress());
		delivery_email.setText(currentDelivery.getEmail());
		delivery_instruction.setText(currentDelivery.getShippingInstructions());
		delivery_name.setText(currentDelivery.getName());
		delivery_phone.setText(currentDelivery.getPhone());
		delivery_province.setText(currentDelivery.getProvince());
    }
    

    @FXML
    void checkOrderType(ActionEvent event) {

    }

    private void sendStateOrderMail(Order order){
        String destEmail = order.getDeliveryInfo().getEmail();
        String invoiceId = String.valueOf(order.getId());
        String recipientName = order.getDeliveryInfo().getName();
        String phone = order.getDeliveryInfo().getPhone();
        String address = order.getDeliveryInfo().getAddress() + ", " + order.getDeliveryInfo().getProvince();
        // String totalAmount = String.valueOf(invoice.getTotalAmount()) + " VND";

        IEmail mail = new SimplejavamailManager();

        String emailContent = "AIMS GROUP-10 NOTIFICATION\n\nYour order has been " + order.getStatus().toString().toUpperCase() + "!\n"
                + "Order ID: " + invoiceId + " (You can later use it to review your order in app)" + "\nRecipient's name: " + recipientName + "\nPhone number: " + phone + "\nAddress: " + address + "\nEmail: " + destEmail
                + "\n\nThank you.";

        mail.sendEmail(destEmail, emailContent, "AIMS GROUP-10 NOTIFICATION");
        
        InformationAlert successAlert = new InformationAlert();
        successAlert.createAlert("Email sent", null, "Notification mail sent to user!");
        successAlert.show();
    }
}
