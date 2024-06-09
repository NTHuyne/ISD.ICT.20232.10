package com.hust.ict.aims;

import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.view.login.LoginHandler;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	 public void start(Stage stage) {
		try {
			LoginHandler loginHandler = new LoginHandler(stage, Configs.LOGIN_PATH);
			loginHandler.setScreenTitle("Login");
			loginHandler.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public static void main(String[] args) {
        launch();
    }
}