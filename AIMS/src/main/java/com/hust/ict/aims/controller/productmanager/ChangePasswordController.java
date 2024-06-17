package com.hust.ict.aims.controller.productmanager;

import com.hust.ict.aims.entity.productmanager.ProductManagerSession;
import com.hust.ict.aims.persistence.dao.user.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.checkerframework.checker.units.qual.A;

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

    public UserDAO userDAO = new UserDAO();

    public void cancelBtn() {
        cancelBtn.getScene().getWindow().hide();
    }

    public Boolean checkMissingField(){
        return emailInput.getText().isEmpty() || curPassword.getText().isEmpty() || newPassword.getText().isEmpty() || confirmNewPassword.getText().isEmpty();
    }

    public Boolean checkEmailField(){
        return !emailInput.getText().equals(ProductManagerSession.getEmail());
    }

    public Boolean checkConfirmPassword(){
        return !newPassword.getText().equals(confirmNewPassword.getText());
    }

    public Boolean checkCurrentPassword(){
        return !curPassword.getText().equals(ProductManagerSession.getPassword());
    }

    public void confirmBtn() {
        if(checkMissingField()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("You must fill in all fields");
            alert.showAndWait();
        } else {
            if(checkEmailField()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("The email address is not correct");
                alert.showAndWait();
            }
            else if(checkCurrentPassword()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("The current password is not correct");
                alert.showAndWait();
            }
            else if(checkConfirmPassword()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("The new confirm password is not matching");
                alert.showAndWait();
            }
            else {
                try {
                    userDAO.updateUserPassword(newPassword.getText(), ProductManagerSession.getId());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Change password successfully");
                    alert.showAndWait();
                    confirmBtn.getScene().getWindow().hide();
                } catch (Exception e){
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("The operation is going wrong");
                    alert.showAndWait();
                }

            }
        }
    }
}
