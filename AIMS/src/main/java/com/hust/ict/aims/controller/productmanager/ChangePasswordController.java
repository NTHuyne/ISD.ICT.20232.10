package com.hust.ict.aims.controller.productmanager;

import com.hust.ict.aims.entity.productmanager.ProductManagerSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {
    @FXML
    public PasswordField curPassword;

    @FXML
    public PasswordField newPassword;

    @FXML
    public PasswordField confirmNewPassword;

    @FXML
    public TextField emailInput;

    @FXML
    public Button confirmBtn;

    @FXML
    public Button cancelBtn;

    @FXML
    public Label username;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText(ProductManagerSession.getUsername());
    }

    public void cancelBtn() {
        cancelBtn.getScene().getWindow().hide();
    }
}
