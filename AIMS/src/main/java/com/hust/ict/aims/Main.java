package com.hust.ict.aims;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.view.home.HomeScreenHandler;
public class Main extends Application {
//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }
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