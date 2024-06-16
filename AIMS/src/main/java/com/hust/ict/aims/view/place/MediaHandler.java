package com.hust.ict.aims.view.place;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.hust.ict.aims.entity.order.OrderMedia;
import com.hust.ict.aims.utils.Utils;
import com.hust.ict.aims.view.FXMLScreenHandler;
import com.hust.ict.aims.view.home.HomeScreenHandler;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MediaHandler extends FXMLScreenHandler {

    private static Logger LOGGER = Utils.getLogger(MediaHandler.class.getName());

    @FXML
    private ImageView mediaImage;

    @FXML
    private Label mediaQuantity;

    @FXML
    private Label mediaTitle;

    @FXML
    private Label mediaTotal;

    @FXML
    private Label mediaUnitPrice;
    private OrderMedia media;
    private HomeScreenHandler home;

    public MediaHandler(String screenPath, OrderMedia media) throws SQLException, IOException {
        super(screenPath);
        setMedia(media);
    }

    public void setMedia(OrderMedia media) {
        this.media = media;
        String imageUrl = media.getMedia().getImageUrl();
        Image image = null;

        try {
            image = new Image(getClass().getResourceAsStream("/assets/images/" + imageUrl));
            if (image.isError()) {
                throw new IOException("Image file not found: " + imageUrl);
            }
            mediaImage.setImage(image);
        } catch (Exception e) {
            LOGGER.warning("Image file not found: " + imageUrl + ". Using default image.");
            image = new Image(getClass().getResourceAsStream("/assets/images/2.png"));
            mediaImage.setImage(image);
        }

        mediaTitle.setText(media.getMedia().getTitle());
        mediaQuantity.setText(String.valueOf(media.getQuantity()));
        mediaUnitPrice.setText(Utils.getCurrencyFormat(media.getMedia().getPrice()));
        mediaTotal.setText(Utils.getCurrencyFormat(media.getMedia().getPrice() * media.getQuantity()));
        LOGGER.info("Set Media Title to " + mediaTitle.getText());
        LOGGER.info("Set Media Unit Price to " + mediaUnitPrice.getText());
        LOGGER.info("Set Media Quantity to " + mediaQuantity.getText());
        LOGGER.info("Set Media Total to " + mediaTotal.getText());
    }
}
