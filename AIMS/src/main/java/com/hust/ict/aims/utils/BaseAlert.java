package com.hust.ict.aims.utils;

import javafx.scene.control.Alert;

public abstract class BaseAlert {
    protected Alert alert;

    public BaseAlert() {
        alert = new Alert(getAlertType());
    }

    protected abstract Alert.AlertType getAlertType();

    public abstract void createAlert(String title, String header, String content);

    public void show() {
        alert.showAndWait();
    }
}
