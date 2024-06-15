package com.hust.ict.aims;

import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.view.login.LoginHandler;
import com.hust.ict.aims.view.order.OrderHandler;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	 public void start(Stage stage) {
		try {
			LoginHandler loginHandler = new LoginHandler(stage, Configs.LOGIN_PATH);
			loginHandler.setScreenTitle("Login");
			loginHandler.show();
//			OrderHandler orderHandler = new OrderHandler(stage, Configs.ORDER_SCREEN_PATH);
//			orderHandler.setScreenTitle("Order");
//			orderHandler.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public static void main(String[] args) {
        launch();
    }
}