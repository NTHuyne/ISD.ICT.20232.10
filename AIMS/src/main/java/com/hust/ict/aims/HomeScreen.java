package com.hust.ict.aims;

import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.view.home.HomeScreenHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class HomeScreen extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            HomeScreenHandler homeHandler = new HomeScreenHandler(primaryStage, Configs.HOME_PATH);
            homeHandler.setScreenTitle("Home Screen");
//                    homeHandler.setImage();
            homeHandler.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
