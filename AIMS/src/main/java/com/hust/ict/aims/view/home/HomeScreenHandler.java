package com.hust.ict.aims.view.home;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import com.hust.ict.aims.controller.HomeController;
import com.hust.ict.aims.controller.ViewCartController;
import com.hust.ict.aims.entity.cart.Cart;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.utils.Utils;
import com.hust.ict.aims.view.BaseScreenHandler;
import com.hust.ict.aims.view.cart.CartScreenHandler;
import com.hust.ict.aims.view.order.OrderHandler;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class HomeScreenHandler extends BaseScreenHandler {

    public static Logger LOGGER = Utils.getLogger(HomeScreenHandler.class.getName());

    @FXML
    private Label numMediaInCart;

    @FXML
    private ImageView aimsImage;

    @FXML
    private Button cartButton;

    @FXML
    private Button orderButton;

    @FXML
    private VBox vboxMedia1;

    @FXML
    private VBox vboxMedia2;

    @FXML
    private VBox vboxMedia3;

    @FXML
    private VBox vboxMedia4;

    @FXML
    private VBox vboxMedia5;

    @FXML
    private HBox hboxMedia;

    @FXML
    private SplitMenuButton splitMenuBtnSearch;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button btnPrevPage;

    @FXML
    private Button btnNextPage;

    @FXML
    private Label lblPageInfo;

    @FXML
    private ChoiceBox<String> choiceBoxOrder;


    private List<MediaHandler> homeItems;
    private int currentPage = 0;
    private int itemsPerPage = 20;

    public HomeScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        setupData();
        setupPagination();
        setupChoiceBoxOrder();
    }

    public Label getNumMediaCartLabel() {
        return this.numMediaInCart;
    }

    @Override
    public HomeController getBController() {
        return (HomeController) super.getBController();
    }

    @Override
    public void show() {
        System.out.println(Cart.getCart().getTotalMedia());
        numMediaInCart.setText(String.valueOf(Cart.getCart().getTotalMedia()) + " media");
        super.show();
    }
    
    public void emptyCart() {
    	Cart.getCart().getListMedia().clear();
    }

    public void setupData() {
        setBController(new HomeController());
        try {
            List<Media> medium = HomeController.getAllMedia();
            homeItems = new ArrayList<>();
            for (Media media : medium) {
                MediaHandler mediaHandler = new MediaHandler(Configs.HOME_MEDIA_PATH, media, this);
                homeItems.add(mediaHandler);
            }
        } catch (SQLException | IOException e) {
            LOGGER.info("Errors occurred: " + e.getMessage());
            e.printStackTrace();
        }

        aimsImage.setOnMouseClicked(e -> {
            currentPage = 1;
            addMediaHome();
        });

        orderButton.setOnMouseClicked(e -> {
            try {
                OrderHandler orderHandler = new OrderHandler(this.stage, Configs.ORDER_SCREEN_PATH);
                orderHandler.setScreenTitle("View Order");
                orderHandler.setHomeScreenHandler(this);
                orderHandler.show();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        cartButton.setOnMouseClicked(e -> {
            CartScreenHandler cartScreen;
            try {
                LOGGER.info("User clicked to view cart");
                cartScreen = new CartScreenHandler(this.stage, Configs.CART_SCREEN_PATH);
                cartScreen.setHomeScreenHandler(this);
                cartScreen.setBController(new ViewCartController());
                cartScreen.requestToViewCart(this);
            } catch (IOException | SQLException e1) {
                LOGGER.info("Error" + e1);
            }
        });

        addMediaHome();
        addMenuItem(0, "All", splitMenuBtnSearch);
        addMenuItem(1, "Book", splitMenuBtnSearch);
        addMenuItem(2, "DVD", splitMenuBtnSearch);
        addMenuItem(3, "CD", splitMenuBtnSearch);
        addMenuItem(4, "LP", splitMenuBtnSearch);
        searchTextField.setOnKeyReleased(event -> handleSearch(event));
    }

    public void setImage() {
        File file1 = new File(Configs.IMAGE_PATH + "/" + "Logo.png");
        Image img1 = new Image(file1.toURI().toString());
        aimsImage.setImage(img1);
    }

    public void addMediaHome() {
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, homeItems.size());

        List<MediaHandler> paginatedItems = new ArrayList<>(homeItems.subList(startIndex, endIndex));

        hboxMedia.getChildren().forEach(node -> {
            VBox vBox = (VBox) node;
            vBox.getChildren().clear();
        });

        while (!paginatedItems.isEmpty()) {
            hboxMedia.getChildren().forEach(node -> {
                VBox vBox = (VBox) node;
                while (vBox.getChildren().size() < 4 && !paginatedItems.isEmpty()) {  // Adjust the size limit for each VBox as needed
                    MediaHandler media = paginatedItems.remove(0);
                    Node content = media.getContent();
                    if (content != null) {
                        vBox.getChildren().add(content);
                    }
                }
            });
        }

        updatePageInfo();
    }

    private void updatePageInfo() {
        int totalItems = homeItems.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        lblPageInfo.setText("Page " + (currentPage + 1) + " of " + totalPages);

        btnPrevPage.setDisable(currentPage == 0);
        btnNextPage.setDisable(currentPage == totalPages - 1);
    }

    private void setupPagination() {
        btnPrevPage.setOnAction(e -> {
            if (currentPage > 0) {
                currentPage--;
                addMediaHome();
            }
        });

        btnNextPage.setOnAction(e -> {
            int totalItems = homeItems.size();
            int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

            if (currentPage < totalPages - 1) {
                currentPage++;
                addMediaHome();
            }
        });
    }

    private void addMenuItem(int position, String text, MenuButton menuButton) {
        MenuItem menuItem = new MenuItem();
        Label label = new Label();
        label.prefWidthProperty().bind(menuButton.widthProperty().subtract(31));
        label.setText(text);
        label.setTextAlignment(TextAlignment.RIGHT);
        menuItem.setGraphic(label);
        menuItem.setOnAction(e -> {
            setBController(new HomeController());
            try {
                List<Media> medium = HomeController.getAllMedia();
                homeItems = new ArrayList<>();
                for (Media media : medium) {
                    if (text.equalsIgnoreCase("All") || media.getMediaTypeName().equalsIgnoreCase(text)) {
                        MediaHandler mediaHandler = new MediaHandler(Configs.HOME_MEDIA_PATH, media, this);
                        homeItems.add(mediaHandler);
                    }
                }
            } catch (SQLException | IOException ex) {
                LOGGER.info("Errors occurred: " + ex.getMessage());
                ex.printStackTrace();
            }

            currentPage = 0;
            addMediaHome();
        });
        menuButton.getItems().add(position, menuItem);
    }

    private void handleSearch(KeyEvent event) {
        String searchTerm = searchTextField.getText().toLowerCase().trim();

        List<MediaHandler> filteredItems = new ArrayList<>();

        for (MediaHandler media : homeItems) {
            if (media.getMedia().getTitle().toLowerCase().contains(searchTerm)) {
                filteredItems.add(media);
            }
        }

        homeItems = filteredItems;
        currentPage = 0;
        addMediaHome();
    }

    private void setupChoiceBoxOrder() {
        choiceBoxOrder.setItems(FXCollections.observableArrayList("Ascending", "Descending"));
        choiceBoxOrder.setValue("Ascending"); // Default selection
        choiceBoxOrder.setOnAction(event -> handleFilter());
    }

    private void handleFilter() {
        String order = choiceBoxOrder.getValue();
        if (order != null) {
            if ("Ascending".equals(order)) {
                sortMedia(Comparator.comparingDouble(o -> o.getMedia().getPrice()));
            } else if ("Descending".equals(order)) {
                sortMedia((o1, o2) -> Double.compare(o2.getMedia().getPrice(), o1.getMedia().getPrice()));
            }
        }

        currentPage = 0;
        addMediaHome();
    }

    private void sortMedia(Comparator<MediaHandler> comparator) {
        homeItems.sort(comparator);
    }
}