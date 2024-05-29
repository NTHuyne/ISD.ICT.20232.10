package com.hust.ict.aims.view.place;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.entity.order.OrderMedia;
import com.hust.ict.aims.utils.Utils;
import com.hust.ict.aims.view.FXMLScreenHandler;
import com.hust.ict.aims.view.home.HomeScreenHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

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
//        mediaImage
        mediaTitle.setText(media.getMedia().getTitle());
        mediaQuantity.setText(String.valueOf(media.getQuantity()));
        mediaUnitPrice.setText(Utils.getCurrencyFormat(media.getMedia().getPrice()));
        mediaTotal.setText(Utils.getCurrencyFormat(media.getPrice()));
        LOGGER.info("Set Media Title to " + mediaTitle.getText());
        LOGGER.info("Set Media Unit Price to " + mediaUnitPrice.getText());
        LOGGER.info("Set Media Quantity to " + mediaQuantity.getText());
        LOGGER.info("Set Media Total to " + mediaTotal.getText());
    }
}
