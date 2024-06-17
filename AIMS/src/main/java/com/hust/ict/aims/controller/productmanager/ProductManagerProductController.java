package com.hust.ict.aims.controller.productmanager;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.order.OrderMedia;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.view.login.LoginHandler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ProductManagerProductController implements Initializable, DataChangedListener {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDataChanged() {
		// TODO Auto-generated method stub
		
	}

	@FXML
    private Button dashboard_btn;

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
    private Button logout_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button medias_btn;

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
    private TableView<Order> orders_tableView;

    @FXML
    private Label productManagerEmail;

    @FXML
    private Label username;

    @FXML
    private Button users_btn;

    @FXML
    void acceptOrder(ActionEvent event) {
    	
    }


    @FXML
    void rejectOrder(ActionEvent event) {

    }

    @FXML
    void selectOrder(MouseEvent event) {
    	Order order = orders_tableView.getSelectionModel().getSelectedItem();

        // int num = medias_tableView.getSelectionModel().getSelectedIndex();
    }
    

    @FXML
    void checkOrderType(ActionEvent event) {

    }

    
    // TODO: Refactor this with productManager
    @FXML
    public void logout() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {

                // hide trang admin
                logout_btn.getScene().getWindow().hide();

                // Quay ve login form
                Stage stage = new Stage();
                LoginHandler loginHandler = new LoginHandler(stage, Configs.LOGIN_PATH);
                loginHandler.setScreenTitle("Login");
                loginHandler.show();
            }

        } catch (Exception e) {
            System.out.println("Uncessfully logout");
            e.printStackTrace();
        }
    }
}
