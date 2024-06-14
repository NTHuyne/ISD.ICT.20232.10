package com.hust.ict.aims.view.home;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
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
    private Button cartImage;

    @FXML
    private VBox vboxMedia1;

    @FXML
    private VBox vboxMedia2;

    @FXML
    private VBox vboxMedia3;

    @FXML
    private VBox vboxMedia4;

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

    private List<MediaHandler> homeItems;
    private int currentPage = 0;
    private int itemsPerPage = 20;

    public HomeScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        setupData();
        setupPagination();
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

        cartImage.setOnMouseClicked(e -> {
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
        addMenuItem(0, "Book", splitMenuBtnSearch);
        addMenuItem(1, "DVD", splitMenuBtnSearch);
        addMenuItem(2, "CD", splitMenuBtnSearch);
        addMenuItem(3, "LP", splitMenuBtnSearch);
        searchTextField.setOnKeyReleased(event -> handleSearch(event));
    }

    public void setImage() {
        File file1 = new File(Configs.IMAGE_PATH + "/" + "Logo.png");
        Image img1 = new Image(file1.toURI().toString());
        aimsImage.setImage(img1);
    }

    public void addMediaHome() {
        int startIndex = currentPage  * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, homeItems.size());

        List<MediaHandler> paginatedItems = homeItems.subList(startIndex, endIndex);

        hboxMedia.getChildren().forEach(node -> {
            VBox vBox = (VBox) node;
            vBox.getChildren().clear();
        });

        while (!paginatedItems.isEmpty()) {
            hboxMedia.getChildren().forEach(node -> {
                VBox vBox = (VBox) node;
                while (vBox.getChildren().size() < 4 && !paginatedItems.isEmpty()) {
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
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage) + 1;

        lblPageInfo.setText("Page " + (currentPage+1) + " of " + totalPages);

        btnPrevPage.setDisable(currentPage == 1);
        btnNextPage.setDisable(currentPage == totalPages);
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

            if (currentPage < totalPages) {
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
            hboxMedia.getChildren().forEach(node -> {
                VBox vBox = (VBox) node;
                vBox.getChildren().clear();
            });

            List<MediaHandler> filteredItems = new ArrayList<>();

            homeItems.forEach(me -> {
                MediaHandler media = (MediaHandler) me;
                if (media.getMedia().getMediaTypeName().equalsIgnoreCase(text)) {
                    filteredItems.add(media);
                }
            });

            homeItems = filteredItems;
            currentPage = -1;
            addMediaHome();
        });
        menuButton.getItems().add(position, menuItem);
    }

    private void handleSearch(KeyEvent event) {
        String searchTerm = searchTextField.getText().toLowerCase().trim();

        List<MediaHandler> filteredItems = new ArrayList<>();

        homeItems.forEach(me -> {
            MediaHandler media = (MediaHandler) me;
            if (media.getMedia().getTitle().toLowerCase().contains(searchTerm)) {
                filteredItems.add(media);
            }
        });

        homeItems = filteredItems;
        currentPage = 0;
        addMediaHome();
    }
}
