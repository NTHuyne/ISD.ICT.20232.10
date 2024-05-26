package com.hust.ict.aims.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ConfirmationAlert extends BaseAlert {

    private Optional<ButtonType> userResponse = Optional.empty();

    @Override
    protected Alert.AlertType getAlertType() {
        return Alert.AlertType.CONFIRMATION;
    }

    @Override
    public void createAlert(String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
    }

    @Override
    public void show() {
        userResponse = alert.showAndWait();
    }

    public boolean isConfirmed() {
        return userResponse.isPresent() && userResponse.get() == ButtonType.OK;
    }
}

