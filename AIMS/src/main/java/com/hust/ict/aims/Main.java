package com.hust.ict.aims;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.view.home.HomeScreenHandler;

public class Main extends Application {
	@Override
	 public void start(Stage primaryStage) {
	        try {
	            HomeScreenHandler homeHandler = new HomeScreenHandler(primaryStage, Configs.HOME_PATH);
	            homeHandler.setScreenTitle("Home Screen");
//	                    homeHandler.setImage();
	            homeHandler.show();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

    public static void main(String[] args) {
        launch();
    }
}