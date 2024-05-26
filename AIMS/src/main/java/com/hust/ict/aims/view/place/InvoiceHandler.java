package com.hust.ict.aims.view.place;

import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.view.BaseScreenHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class InvoiceHandler extends BaseScreenHandler {

    @FXML
    private ImageView aimsImage;

    @FXML
    private Button cancelOrderButton;

    @FXML
    private VBox mediaListVBox;

    @FXML
    private Label pageTitle;

    @FXML
    private Button payOrderButton;

    public InvoiceHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        File file = new File(Configs.IMAGE_PATH + "/Logo.png");
        Image im = new Image(file.toURI().toString());
        aimsImage.setImage(im);

        setUpData();

        // on mouse clicked, we back to home
        aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });

        payOrderButton.setOnMouseClicked(e -> {
            requestPayOrder();
        });

        cancelOrderButton.setOnMouseClicked(e -> {
            // Go back to cart screen

        });


    }

    public void setUpData() {
        
    }

    public void requestPayOrder() {

    }
}
