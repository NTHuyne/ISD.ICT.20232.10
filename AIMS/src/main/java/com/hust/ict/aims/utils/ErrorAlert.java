package com.hust.ict.aims.utils;

import javafx.scene.control.Alert;

public class ErrorAlert extends BaseAlert {

    @Override
    protected Alert.AlertType getAlertType() {
        return Alert.AlertType.ERROR;
    }

    @Override
    public void createAlert(String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
    }
}

