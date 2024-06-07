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
	 public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/login.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		stage.setTitle("AIMS");
		stage.setScene(scene);
		stage.show();
	    }

    public static void main(String[] args) {
        launch();
    }
}