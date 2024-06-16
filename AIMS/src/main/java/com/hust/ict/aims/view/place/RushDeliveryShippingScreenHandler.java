package com.hust.ict.aims.view.place;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import com.hust.ict.aims.controller.PlaceOrderController;
import com.hust.ict.aims.entity.invoice.Invoice;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.exception.placement.InvalidRushDeliveryTimeException;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.utils.ErrorAlert;
import com.hust.ict.aims.view.BaseScreenHandler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class RushDeliveryShippingScreenHandler extends BaseScreenHandler {

    @FXML
    private ImageView aimsImage;

    @FXML
    private Button cancelRushDeliveryButton;

    @FXML
    private ChoiceBox<Integer> dayChoiceBox;

    @FXML
    private ChoiceBox<Integer> hourChoiceBox;

    @FXML
    private TextField instructionsField;

    @FXML
    private ChoiceBox<Integer> minuteChoiceBox;

    @FXML
    private ChoiceBox<Integer> monthChoiceBox;

    @FXML
    private Button proceedRushDeliveryButton;

    @FXML
    private ChoiceBox<Integer> yearChoiceBox;

    public RushDeliveryShippingScreenHandler(Stage stage, String screenPath, PlaceOrderController placeOrderController, Invoice invoice) throws IOException {
        super(stage, screenPath);
        String imagePath = "/assets/images/Logo.png";
        Image im = new Image(getClass().getResourceAsStream(imagePath));
        aimsImage.setImage(im);

        Order order = invoice.getOrder();

        // on mouse clicked, we back to home
        aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });

        for(int i=0; i<3; i++) {
            LocalDate currentDate = LocalDate.now().plusDays(i);
            dayChoiceBox.getItems().add(currentDate.getDayOfMonth());
            if(monthChoiceBox.getItems().isEmpty() ||
                    !monthChoiceBox.getItems().contains(currentDate.getMonthValue())) {
				monthChoiceBox.getItems().add(currentDate.getMonthValue());
			}
            if(yearChoiceBox.getItems().isEmpty() ||
                    !yearChoiceBox.getItems().contains(currentDate.getYear())) {
				yearChoiceBox.getItems().add(currentDate.getYear());
			}
        }

        LocalTime currentTime = LocalTime.now();
        for(int i=0; i<24; i++) {
			hourChoiceBox.getItems().add(i);
		}
        for(int j=0; j<60; j++) {
			minuteChoiceBox.getItems().add(j);
		}

        proceedRushDeliveryButton.setOnMouseClicked(e -> {
            try{

                String date = yearChoiceBox.getValue() + "-" +
                        (monthChoiceBox.getValue() < 10 ? ("0") : "") + monthChoiceBox.getValue() + "-" +
                        (dayChoiceBox.getValue() < 10 ? "0" : "") + dayChoiceBox.getValue();
                LocalDate localDate = LocalDate.parse(date);
                String time = (hourChoiceBox.getValue() < 10 ? "0" : "") + hourChoiceBox.getValue() + ":" +
                        (minuteChoiceBox.getValue() < 10 ? "0" : "") + minuteChoiceBox.getValue() + ":59";
                LocalTime localTime = LocalTime.parse(time);
                if (localDate.isEqual(LocalDate.now()) && localTime.isBefore(currentTime)) {
					throw new InvalidRushDeliveryTimeException();
				}
//                placeOrderController.canPlaceRushOrder(order);
//                Order regularOrder = placeOrderController.categorizeRegularOrder(order);
//                Order rushOrder = placeOrderController.categorizeRushOrder(order);
                order.setLocalDate(localDate); order.setLocalTime(localTime);
                placeOrderController.categorizeOrder(order);
                RushDeliveryInvoiceHandler rushDeliveryInvoiceHandler = new RushDeliveryInvoiceHandler(this.stage, Configs.RUSH_DELIVERY_INVOICE_PATH, order, placeOrderController);
                rushDeliveryInvoiceHandler.setHomeScreenHandler(homeScreenHandler);
                rushDeliveryInvoiceHandler.setScreenTitle("Rush Delivery Invoice");
                rushDeliveryInvoiceHandler.setBController(placeOrderController);
                rushDeliveryInvoiceHandler.show();
            }
            catch(IOException exp) {
                exp.printStackTrace();
            }
            catch(InvalidRushDeliveryTimeException e1) {
                ErrorAlert errorAlert = new ErrorAlert();
                errorAlert.createAlert("Error message", null, "Rush delivery time is earlier than current time!");
                errorAlert.show();
                throw new RuntimeException("Rush order unsupported");
            }
        });

        cancelRushDeliveryButton.setOnMouseClicked(e -> {
            // place rush order
            this.getPreviousScreen().show();
        });
    }
}
