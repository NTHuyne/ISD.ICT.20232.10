package com.hust.ict.aims.view.home;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.hust.ict.aims.entity.cart.Cart;
import com.hust.ict.aims.entity.cart.CartMedia;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.utils.ErrorAlert;
import com.hust.ict.aims.utils.Utils;
import com.hust.ict.aims.view.BaseScreenHandler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MediaHandler extends BaseScreenHandler {

    @FXML
    private ImageView mediaImage;

    @FXML
    private Label mediaTitle;

    @FXML
    private Label mediaPrice;

    @FXML
    private Label mediaAvail;

    @FXML
    private Spinner<Integer> spinnerChangeNumber;

    @FXML
    private Button addToCartBtn;

    private static Logger LOGGER = Utils.getLogger(MediaHandler.class.getName());
    private Media media;


    public MediaHandler(String screenPath, Media media, HomeScreenHandler home) throws SQLException, IOException {
        super(screenPath);
        this.media = media;
        addToCartBtn.setOnMouseClicked(event -> {
            try {
                if (spinnerChangeNumber.getValue() > media.getTotalQuantity()) {
                    throw new RuntimeException("Media Not Available!");
                }
                Cart cart = Cart.getCart();
                // if media already in cart then we will increase the quantity by 1 instead of create the new cartMedia
                CartMedia mediaInCart = home.getBController().checkMediaInCart(media);
                if (mediaInCart != null) {
                    mediaInCart.setQuantity(mediaInCart.getQuantity() + 1);
                } else {
                    CartMedia cartMedia = new CartMedia(media, cart, spinnerChangeNumber.getValue(), media.getPrice());
                    cart.getListMedia().add(cartMedia);
                    LOGGER.info("Added " + cartMedia.getQuantity() + " " + media.getTitle() + " to cart");
                }

                // subtract the quantity and redisplay
                media.setTotalQuantity(media.getTotalQuantity() - spinnerChangeNumber.getValue());
                mediaAvail.setText(String.valueOf(media.getTotalQuantity()));
                home.getNumMediaCartLabel().setText(String.valueOf(cart.getTotalMedia() + " media"));
            } catch (Exception exp) {
                try {
                    String message = "Not enough media:\nRequired: " + spinnerChangeNumber.getValue() + "\nAvail: " + media.getTotalQuantity();
                    LOGGER.severe(message);
                    ErrorAlert errorAlert = new ErrorAlert();
                    errorAlert.createAlert("Error Message", null, message);
                    errorAlert.show();
                } catch (Exception e) {
                    LOGGER.severe("Cannot add media to cart: ");
                }

            }
        });
        mediaImage.setOnMouseClicked(event -> {
            try {
                showMediaDetails(media);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        setMediaInfo();
    }

    /**
     * @return Media
     */
    public Media getMedia() {
        return media;
    }


    /**
     * @throws SQLException
     */
    private void setMediaInfo() throws SQLException {
        String imageUrl = media.getImageUrl();
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

        mediaImage.setFitHeight(132);
        mediaImage.setFitWidth(99);

        mediaTitle.setText(media.getTitle());
        mediaPrice.setText(Utils.getCurrencyFormat(media.getPrice()));
        mediaAvail.setText(Integer.toString(media.getTotalQuantity()));
        spinnerChangeNumber.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1)
        );
    }

    private void showMediaDetails(Media media) throws IOException {
        MediaDetailHandler mediaDetailHandler = new MediaDetailHandler(this.stage, Configs.HOME_MEDIA_DETAIL_PATH, media);
        mediaDetailHandler.setScreenTitle("Detail media: " + media.getTitle());
        mediaDetailHandler.show();
    }
}
