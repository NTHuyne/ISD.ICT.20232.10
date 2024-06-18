package com.hust.ict.aims.controller.productmanager;

import com.hust.ict.aims.entity.productmanager.ProductManagerSession;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.view.login.LoginHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ProductManagerController implements Initializable {

    @FXML
    public Button changepassword_btn;
    
    @FXML
    private AnchorPane displayPane;

    private Pane orderPane;
    private Pane mediasPane;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();

        try {
        	this.orderPane = FXMLLoader.load(getClass().getResource(Configs.PRODUCT_MANAGER_ORDER_PATH));
        	this.mediasPane = FXMLLoader.load(getClass().getResource(Configs.PRODUCT_MANAGER_MEDIA_PATH));
        	
        	displayPane.getChildren().add(this.orderPane);
        	displayPane.getChildren().add(this.mediasPane);
        	
        	this.orderPane.setVisible(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    @FXML
    private Label productManagerEmail;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private Label username;

    private Alert alert;

    public void logout() {
        try {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
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

    public void displayUsername() {
        String user = ProductManagerSession.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        username.setText(user);
        productManagerEmail.setText(ProductManagerSession.email);
    }

    public void changePasswordBtn() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource(Configs.CHANGE_PASSWORD_PATH));
        Stage stage1 = new Stage();
        Scene scene1 = new Scene(root);

        stage1.setTitle("Change account password");
        stage1.setScene(scene1);
        stage1.show();
    }
    
    @FXML
    void manageOrders(MouseEvent event) {
    	this.mediasPane.setVisible(false);
    	this.orderPane.setVisible(true);
    	System.out.println("Already");
    }
    
    @FXML
    void manageMedias(MouseEvent event) {
    	this.mediasPane.setVisible(true);
    	this.orderPane.setVisible(false);
    	System.out.println("Already 2");
    }
}
