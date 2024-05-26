package com.hust.ict.aims.view.home;


import com.hust.ict.aims.entity.media.*;
import com.hust.ict.aims.entity.cart.*;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.utils.ErrorAlert;
import com.hust.ict.aims.utils.Utils;
import com.hust.ict.aims.view.BaseScreenHandler;
import com.hust.ict.aims.view.FXMLScreenHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;


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
    private HomeScreenHandler home;

    public MediaHandler(String screenPath, Media media, HomeScreenHandler home) throws SQLException, IOException{
        super(screenPath);
        this.media = media;
        this.home = home;
        addToCartBtn.setOnMouseClicked(event -> {
            try {
                if (spinnerChangeNumber.getValue() > media.getQuantity()) {
                    throw new RuntimeException("Media Not Available!");
                };
                Cart cart = Cart.getCart();
                // if media already in cart then we will increase the quantity by 1 instead of create the new cartMedia
                CartMedia mediaInCart = home.getBController().checkMediaInCart(media);
                if (mediaInCart != null) {
                    mediaInCart.setQuantity(mediaInCart.getQuantity() + 1);
                }else{
                    CartMedia cartMedia = new CartMedia(media, cart, spinnerChangeNumber.getValue(), media.getPrice());
                    cart.getListMedia().add(cartMedia);
                    LOGGER.info("Added " + cartMedia.getQuantity() + " " + media.getTitle() + " to cart");
                }

                // subtract the quantity and redisplay
                media.setQuantity(media.getQuantity() - spinnerChangeNumber.getValue());
                mediaAvail.setText(String.valueOf(media.getQuantity()));
                home.getNumMediaCartLabel().setText(String.valueOf(cart.getTotalMedia() + " media"));
            } catch (Exception exp) {
                try {
                    String message = "Not enough media:\nRequired: " + spinnerChangeNumber.getValue() + "\nAvail: " + media.getQuantity();
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
    public Media getMedia(){
        return media;
    }


    /**
     * @throws SQLException
     */
    private void setMediaInfo() throws SQLException {
        // set the cover image of media
//        File file = new File(media.getImageUrl());
//        int i = (int) ((Math.random() * (6 - 1)) + 1);
//        String path = Configs.IMAGE_PATH + i +".png";

        String path = Configs.IMAGE_PATH + "/media/2.png";
        File file = new File(path);
        Image image = new Image(file.toURI().toString());
        mediaImage.setFitHeight(160);
        mediaImage.setFitWidth(152);
//        mediaImage.setImage(image);

        mediaTitle.setText(media.getTitle());
        mediaPrice.setText(Utils.getCurrencyFormat(media.getPrice()));
        mediaAvail.setText(Integer.toString(media.getQuantity()));
        spinnerChangeNumber.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1)
        );
//        setImage(mediaImage, path);

    }

    private void showMediaDetails(Media media) throws IOException {
        MediaDetailHandler mediaDetailHandler = new MediaDetailHandler(this.stage, Configs.HOME_MEDIA_DETAIL_PATH, media);
        mediaDetailHandler.setScreenTitle("Detail media:" + media.getTitle());
        mediaDetailHandler.show();
    }
}
