package com.hust.ict.aims.view.cart;

import com.hust.ict.aims.controller.PlaceOrderController;
import com.hust.ict.aims.controller.ViewCartController;
import com.hust.ict.aims.entity.cart.CartMedia;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.utils.ErrorAlert;
import com.hust.ict.aims.utils.Utils;
import com.hust.ict.aims.view.BaseScreenHandler;
import com.hust.ict.aims.view.home.HomeScreenHandler;
import com.hust.ict.aims.view.place.ShippingScreenHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class CartScreenHandler extends BaseScreenHandler {

    private static Logger LOGGER = Utils.getLogger(CartScreenHandler.class.getName());

    @FXML
    private ImageView aimsImage;

    @FXML
    private Label subtotalLabel;

    @FXML
    private Label VATLabel;

    @FXML
    private Label totalLabel;

    @FXML
    VBox vboxCart;

    @FXML
    private Button placeOrderBtn;


    ClassLoader classLoader = getClass().getClassLoader();

    public CartScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        File file = new File(Configs.IMAGE_PATH + "/Logo.png");
        Image im = new Image(file.toURI().toString());
        aimsImage.setImage(im);

        // on mouse clicked, we back to home
        aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });

        placeOrderBtn.setOnMouseClicked(e -> {
            LOGGER.info("Place Order button clicked");
            try {
                requestToPlaceOrder();
            } catch (Exception exp) {
                LOGGER.severe("Cannot place the order, see the logs");
                ErrorAlert errorAlert = new ErrorAlert();
                errorAlert.createAlert("Error Message", null, "Cannot place the order");
                errorAlert.show();
                exp.printStackTrace();
                throw new RuntimeException("Cannot place the order");
            }
        });
    }


    public ViewCartController getBController(){
        return (ViewCartController) super.getBController();
    }

    public void requestToViewCart(HomeScreenHandler homeScreenHandler) throws SQLException {
        setPreviousScreen(homeScreenHandler);
        setScreenTitle("Cart Screen");
        getBController().checkAvailabilityOfProduct();
        displayCartWithMediaAvailability();
        show();
    }

    public void requestToPlaceOrder(){
        PlaceOrderController placeOrderController = new PlaceOrderController();
        if(placeOrderController.getListCartMedia().isEmpty()) return;
        placeOrderController.placeOrder();
//        displayCartWithMediaAvailability();
        try {
            ShippingScreenHandler shippingScreenHandler = new ShippingScreenHandler(this.stage, Configs.SHIPPING_SCREEN_PATH, placeOrderController);
            shippingScreenHandler.setHomeScreenHandler(homeScreenHandler);
            shippingScreenHandler.setScreenTitle("Shipping");
            shippingScreenHandler.setBController(placeOrderController);
            shippingScreenHandler.show();
        }
        catch(Exception e) {
            displayCartWithMediaAvailability();
            e.printStackTrace();
        }
    }

    public void updateCart() throws SQLException{
        getBController().checkAvailabilityOfProduct();
        displayCartWithMediaAvailability();
    }

    void updateCartAmount(){
        // calculate subtotal and amount
        int subtotal = getBController().getCartSubtotal();
        int vat = (int)((Configs.PERCENT_VAT/100)*subtotal);
        int amount = subtotal + vat;
        LOGGER.info("amount" + amount);

        // update subtotal and amount of Cart
        subtotalLabel.setText(Utils.getCurrencyFormat(subtotal));
        VATLabel.setText(Utils.getCurrencyFormat(vat));
        totalLabel.setText(Utils.getCurrencyFormat(amount));
    }

    private void displayCartWithMediaAvailability(){
        // clear all old cartMedia
        vboxCart.getChildren().clear();

        List lstMedia = getBController().getListCartMedia();

        try {
            for (Object cm : lstMedia) {
                // display the attribute of vboxCart media
                CartMedia cartMedia = (CartMedia) cm;
                MediaHandler mediaCartScreen = new MediaHandler(Configs.CART_MEDIA_PATH, this);
                mediaCartScreen.setCartMedia(cartMedia);
                // add spinner
                vboxCart.getChildren().add(mediaCartScreen.getContent());
            }
            // calculate subtotal and amount
            updateCartAmount();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
