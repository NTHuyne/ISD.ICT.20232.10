package com.hust.ict.aims.view.cart;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.hust.ict.aims.entity.cart.Cart;
import com.hust.ict.aims.entity.cart.CartMedia;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.utils.Utils;
import com.hust.ict.aims.view.FXMLScreenHandler;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class MediaHandler extends FXMLScreenHandler {

    private static Logger LOGGER = Utils.getLogger(MediaHandler.class.getName());

    @FXML
    private HBox hboxMedia;

    @FXML
    private ImageView image;

    @FXML
    private Label labelOutOfStock;

    @FXML
    private Label quantity;

    @FXML
    private Label title;

    @FXML
    private Label price;

    @FXML
    private Button deleteBtn;

    private CartScreenHandler cartScreen;

    private CartMedia cartMedia;

    private Spinner<Integer> spinner;


    public MediaHandler(String screenPath, CartScreenHandler cartScreen) throws IOException {
        super(screenPath);
        this.cartScreen = cartScreen;
        hboxMedia.setAlignment(Pos.CENTER);
    }

    public void setCartMedia(CartMedia cartMedia) {
        this.cartMedia = cartMedia;
        setMediaInfo();
    }

    private void setMediaInfo() {
        title.setText(cartMedia.getMedia().getTitle());
        price.setText(Utils.getCurrencyFormat(cartMedia.getPrice()));
        quantity.setText(String.valueOf(cartMedia.getQuantity()));

        try {
            Image im = new Image(getClass().getResourceAsStream("/assets/images/" + cartMedia.getMedia().getImageUrl()));
            image.setImage(im);
            image.setPreserveRatio(false);
            image.setFitHeight(110);
            image.setFitWidth(92);
        } catch (Exception e){
            LOGGER.warning("Image file not found: " + cartMedia.getMedia().getImageUrl() + ". Using default image.");
            Image im = new Image(getClass().getResourceAsStream("/assets/images/2.png"));
            image.setImage(im);
            image.setPreserveRatio(false);
            image.setFitHeight(110);
            image.setFitWidth(92);
        }

        deleteBtn.setFont(Configs.REGULAR_FONT);
        deleteBtn.setOnMouseClicked(e -> {
            try {
                Cart.getCart().removeCartMedia(cartMedia);
                cartScreen.updateCart();
            } catch (SQLException exp) {
                exp.printStackTrace();
            }
        });
        initializeSpinner();
    }

    private void initializeSpinner(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, cartMedia.getQuantity());
        spinner = new Spinner<>(valueFactory);
        spinner.setOnMouseClicked( e -> {
            try {
                int numOfProd = this.spinner.getValue();
                int remainQuantity = cartMedia.getMedia().getTotalQuantity();
                LOGGER.info("NumOfProd: " + numOfProd + " -- remainOfProd: " + remainQuantity);
                if (numOfProd > remainQuantity){
                    LOGGER.info("Product " + cartMedia.getMedia().getTitle() + " only remains " + remainQuantity + " (required " + numOfProd + ")");
                    labelOutOfStock.setText("Sorry, Only " + remainQuantity + " remain in stock");
                    spinner.getValueFactory().setValue(remainQuantity);
                    numOfProd = remainQuantity;
                }

                // Update quantity of mediaCart in useCart
                cartMedia.setQuantity(numOfProd);

                // Update the total of mediaCart
                price.setText(Utils.getCurrencyFormat(numOfProd * cartMedia.getPrice()));

                // Update subtotal and amount of Cart
                cartScreen.updateCartAmount();

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });
    }
}
