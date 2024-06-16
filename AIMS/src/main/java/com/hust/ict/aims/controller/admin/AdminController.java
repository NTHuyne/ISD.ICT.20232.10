package com.hust.ict.aims.controller.admin;

import com.hust.ict.aims.controller.PlaceOrderController;
import com.hust.ict.aims.entity.admin.AdminSession;
import com.hust.ict.aims.entity.user.User;
import com.hust.ict.aims.persistence.dao.user.UserDAO;
import com.hust.ict.aims.utils.ConfirmationAlert;
import com.hust.ict.aims.utils.ErrorAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TextField emailField;

    @FXML
    private TableColumn<User, Boolean> isAdminColumn;

    @FXML
    private Button logout_btn;

    @FXML
    private TableView<User> userTableView;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TextField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button userDeleteButton;

    @FXML
    private TableColumn<User, Integer> userIDColumn;

    @FXML
    private Button userUpdateButton;

    @FXML
    private Label username;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TextField usernameField;

    @FXML
    private Label adminEmail;

    private UserDAO userDAO;

    private ObservableList<User> userListData;

    @FXML
    private Button userAddButton;

    @FXML
    private Button clearBtn;

    @FXML
    void logout(ActionEvent event) {
        try{
            ConfirmationAlert confAlert = new ConfirmationAlert();
            confAlert.createAlert("Hello", null, "Are you sure to log out?");
            confAlert.show();
            if(confAlert.isConfirmed()) {
                logout_btn.getScene().getWindow().hide();
            }
        }
        catch(Exception e) {
            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Hello", null, "Can not log out!");
            errorAlert.show();
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.userDAO = new UserDAO();

        displayUsername();
        userRolesList();
        userShowData();

        userUpdateButton.setDisable(true);
        userDeleteButton.setDisable(true);

        userAddButton.setOnMouseClicked(e -> {
            addUser();
        });

        userUpdateButton.setOnMouseClicked(e -> {
            updateUser();
        });

        userDeleteButton.setOnMouseClicked(e -> {
            deleteUser();
        });

        clearBtn.setOnMouseClicked(e -> {
            clearAllFields();
        });
    }

    public void clearAllFields() {
        usernameField.setText("");
        passwordField.setText("");
        emailField.setText("");
        roleComboBox.setDisable(false);
        roleComboBox.setValue("");
    }

    public void addUser() {
        if(usernameField.getText().isEmpty() ||
                passwordField.getText().isEmpty() ||
                !PlaceOrderController.validateEmailField(emailField) ||
                roleComboBox.getValue().isEmpty()) {
            ErrorAlert errAlert = new ErrorAlert();
            errAlert.createAlert("Error message", null, "Please fill all blank fields!");
            errAlert.show();
            throw new RuntimeException();
        }

        if(roleComboBox.getValue().equals("Admin")) {
            ErrorAlert errAlert = new ErrorAlert();
            errAlert.createAlert("Error message", null, "Can not add a new admin.");
            errAlert.show();
            throw new RuntimeException();
        }

        try {
            Boolean isAdmin = roleComboBox.getValue().equals("Admin");
            User newUser = new User(usernameField.getText(), passwordField.getText(), isAdmin, emailField.getText());
            userDAO.addUser(newUser);
            UserEmailSender.sendUserEmail(newUser, "Your new AIMS' account created by \n" + AdminSession.getEmail() + " \n");
            userShowData();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUser() {
        if(usernameField.getText().isEmpty() ||
                passwordField.getText().isEmpty() ||
                !PlaceOrderController.validateEmailField(emailField) ||
                roleComboBox.getValue().isEmpty()) {
            ErrorAlert errAlert = new ErrorAlert();
            errAlert.createAlert("Error message", null, "Please fill all blank fields!");
            errAlert.show();
            throw new RuntimeException();
        }

        try{
            Boolean isAdmin = roleComboBox.getValue().equals("Admin");
            User user = userTableView.getSelectionModel().getSelectedItem();

            if(user.getIsAdmin() && user.getIsAdmin() != isAdmin) {
                ErrorAlert errorAlert = new ErrorAlert();
                errorAlert.createAlert("Error mess", null, "Can not change the role of admin");
                errorAlert.show();
                throw new RuntimeException();
            }

            if(user.getIsAdmin() && user.getId() != AdminSession.id) {
                ErrorAlert errorAlert = new ErrorAlert();
                errorAlert.createAlert("Error mess", null, "Can not change personal info of other administrators");
                errorAlert.show();
                throw new RuntimeException();
            }

            user.setUsername(usernameField.getText());
            user.setPassword(passwordField.getText());
            user.setEmail(emailField.getText());
            user.setIsAdmin(isAdmin);

            userDAO.updateUser(user);
            UserEmailSender.sendUserEmail(user, "Your AIMS' account has been updated by " + AdminSession.getEmail() + " \n");
            userShowData();
            deselectUser();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser() {
        User user = userTableView.getSelectionModel().getSelectedItem();
        if(user.getId() == AdminSession.id) {
            ErrorAlert errAlert = new ErrorAlert();
            errAlert.createAlert("Error mess", null, "Can not delete because the user\'s session is currently in use.");
            errAlert.show();
            throw new RuntimeException();
        }
        try {
            userDAO.deleteUser(user);
            UserEmailSender.sendUserEmail(user, "Your AIMS' account has been deleted by " + AdminSession.getEmail() + " \n");
            userShowData();
            deselectUser();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void displayUsername() {
        String user = AdminSession.username;
        AdminSession.username = user;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        username.setText(user);
        adminEmail.setText(AdminSession.email);
    }

    private int selectedIndex = -1;

    public void deselectUser() {
        userTableView.getSelectionModel().clearSelection(selectedIndex);
        selectedIndex = -1;

        clearAllFields();

        userAddButton.setDisable(false);
        userUpdateButton.setDisable(true);
        userDeleteButton.setDisable(true);
        usernameField.setDisable(false);
        emailField.setDisable(false);
        clearBtn.setDisable(false);
    }

    public void selectUser() {
        User user = userTableView.getSelectionModel().getSelectedItem();
        int idx = userTableView.getSelectionModel().getSelectedIndex();
        if(idx != selectedIndex) {
            selectedIndex = idx;

            usernameField.setText(user.getUsername());
            passwordField.setText(user.getPassword());
            emailField.setText(user.getEmail());
            roleComboBox.setValue((user.getIsAdmin() ? "Admin" : "Product Manager"));
            if (roleComboBox.getValue().equals("Admin")){
                roleComboBox.setDisable(true);
            }
            usernameField.setDisable(true);
            emailField.setDisable(true);

            AdminSession.password = user.getPassword();
            AdminSession.isAdmin = user.getIsAdmin();
            userAddButton.setDisable(true);
            userUpdateButton.setDisable(false);
            userDeleteButton.setDisable(false);
            clearBtn.setDisable(true);
        }
        else {
            deselectUser();
        }
        if(idx <= -1) return;
    }

    public void userRolesList() {
        String roles[] = {"Admin", "Product Manager"};
        List<String> rolesList = new ArrayList<>();
        for(String role : roles) {
            rolesList.add(role);
        }

        ObservableList<String> rolesObservableList = FXCollections.observableArrayList(rolesList);
        roleComboBox.setItems(rolesObservableList);
    }

    public void userShowData() {
        try {
            userListData = FXCollections.observableArrayList(userDAO.getAll());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        isAdminColumn.setCellValueFactory(new PropertyValueFactory<>("isAdmin"));

        userTableView.setItems(userListData);
    }
}

