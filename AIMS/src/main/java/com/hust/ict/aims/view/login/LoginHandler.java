package com.hust.ict.aims.view.login;

import java.io.IOException;
import java.util.logging.Logger;

import com.hust.ict.aims.HomeScreen;
import com.hust.ict.aims.controller.LoginController;
import com.hust.ict.aims.entity.admin.AdminSession;
import com.hust.ict.aims.entity.productmanager.ProductManagerSession;
import com.hust.ict.aims.entity.user.User;
import com.hust.ict.aims.exception.LoginAccountException;
import com.hust.ict.aims.persistence.dao.user.UserDAO;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.utils.Utils;
import com.hust.ict.aims.view.BaseScreenHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginHandler extends BaseScreenHandler {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;

    @FXML
    private Hyperlink homeHyperLink;

    public Alert alert;

    public static Logger LOGGER = Utils.getLogger(LoginHandler.class.getName());

    private LoginController loginController = new LoginController();

    public LoginHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        homeHyperLink.setOnAction(actionEvent -> {
            try {
                Stage root = new Stage();
                HomeScreen homeScreen = new HomeScreen();
                homeScreen.start(root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        loginBtn.setOnAction(actionEvent -> loginBtn());
    }

    public void loginBtn(){
        if (username.getText().isEmpty() || password.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("You must fill in username and password");
            alert.showAndWait();
        } else {
            try {
                User user = loginController.validateLogin(username.getText(), password.getText(), new UserDAO());
                if (user.getIsAdmin()){
                    System.out.println("Admin logged in");
                    AdminSession.username = user.getUsername();
                    AdminSession.email = user.getEmail();
                    AdminSession.id = user.getId();
                    Parent root = FXMLLoader.load(getClass().getResource(Configs.ADMIN_PATH));
                    Stage stage1 = new Stage();
                    Scene scene1 = new Scene(root);

                    stage1.setTitle("AIMS");
                    stage1.setScene(scene1);
                    stage1.show();
                }
                else {
                    ProductManagerSession.setPassword(user.getPassword());
                    ProductManagerSession.setId(user.getId());
                    ProductManagerSession.username = user.getUsername();
                    ProductManagerSession.email = user.getEmail();
                    Stage stage1 = (Stage) username.getScene().getWindow();
                    stage1.close();
                    Parent root = FXMLLoader.load(getClass().getResource(Configs.PRODUCT_MANAGER_PATH));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    stage.setTitle("AIMS");
                    stage.setMinWidth(1400);
                    stage.setMinHeight(800);

                    stage.setScene(scene);
                    stage.show();
                }
            } catch (LoginAccountException | IOException e){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Wrong username or password");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }


}
