package com.hust.ict.aims.controller.productmanager;

import com.hust.ict.aims.entity.media.Book;
import com.hust.ict.aims.entity.media.CdAndLp;
import com.hust.ict.aims.entity.media.Dvd;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.entity.productmanager.ProductManagerSession;
import com.hust.ict.aims.persistence.dao.media.BookDAO;
import com.hust.ict.aims.persistence.dao.media.CDDAO;
import com.hust.ict.aims.persistence.dao.media.DVDDAO;
import com.hust.ict.aims.persistence.dao.media.MediaDAO;
import com.hust.ict.aims.utils.Configs;
import com.hust.ict.aims.utils.ConfirmationAlert;
import com.hust.ict.aims.utils.ErrorAlert;
import com.hust.ict.aims.utils.InformationAlert;
import com.hust.ict.aims.view.login.LoginHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ProductManagerController implements Initializable, DataChangedListener {

    @Override
    public void onDataChanged() {
        mediaShowData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.mediaDAO = new MediaDAO();
        this.bookScreen = new BookScreen(this, new BookDAO());
        this.cdAndLpScreen = new CDAndLPScreen(this, new CDDAO());
        this.dvdScreen = new DVDScreen(this, new DVDDAO());
        
        displayUsername();
        mediasCategoryList();
        mediasRushOrderSupportList();
        mediaShowData();

    }

    private BookScreen bookScreen;
    private CDAndLPScreen cdAndLpScreen;
    private DVDScreen dvdScreen;
    
    private MediaDAO mediaDAO;

    @FXML
    private Label productManagerEmail;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private ComboBox<String> media_category;

    @FXML
    private ComboBox<String> media_rushOrderSupport;

    @FXML
    private TextArea media_description;

    @FXML
    private Button media_importBtn;

    @FXML
    private TextField media_price;

    @FXML
    private TextField media_quantity;
    
    @FXML
    private TextField media_weight;

    @FXML
    private Button medias_addBtn;

    @FXML
    private Button medias_btn;

    @FXML
    private Button medias_clearBtn;

    @FXML
    private TableColumn<Media, String> medias_col_category;

    @FXML
    private TableColumn<Media, String> medias_col_desciption;

    @FXML
    private TableColumn<Media, Integer> medias_col_id;

    @FXML
    private TableColumn<Media, Date> medias_col_importDate;

    @FXML
    private TableColumn<Media, Integer> medias_col_price;

    @FXML
    private TableColumn<Media, Integer> medias_col_quantity;
    
    @FXML
    private TableColumn<Media, Integer> medias_col_weight;

    @FXML
    private TableColumn<Media, String> medias_col_title;

    @FXML
    private TableColumn<Media, Integer> medias_col_value;

    @FXML
    private TableColumn<Media, Boolean> medias_col_rushOrderSupport;

    @FXML
    private TableColumn<Media, String> medias_col_barcode;

    @FXML
    private TableColumn<Media, String> medias_col_productDimension;

    @FXML
    private Button medias_deleteBtn;

    @FXML
    private AnchorPane medias_form;

    @FXML
    private TextField media_title;

    @FXML
    private TextField media_barcode;

    @FXML
    private TextField media_productDimension;


    @FXML
    private ImageView medias_imageView;

    @FXML
    private TableView<Media> medias_tableView;

    @FXML
    private Button medias_updateBtn;

    @FXML
    private Label username;

    @FXML
    private Button users_btn;

    private Alert alert;

    private String[] categoryList = {"Book", "CD", "LP", "DVD"};

    private String[] rushOrderSupportList = {"Yes", "No"};

    private Image image;
    private ObservableList<Media> mediaListData;
    private int priceUpdateCount = 0;
    private LocalDate lastPriceUpdateDate = null;

    public void mediasAddBtn() {
        if (this.missingMediaField()) {        	
            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Error Message", null, "Please fill all blank fields");
            errorAlert.show();
        }
        try {
        	if (mediaDAO.isTitleTaken(media_title.getText())) {
                throw new IllegalArgumentException(media_title.getText() + " is already taken");
            }
        	
            String category = media_category.getValue().toString();
            // Factory
            Media newMedia = switch (category.toUpperCase()) {
	        	case "BOOK"		-> new Book();
	        	case "CD"       -> new CdAndLp(true);
                case "LP"       -> new CdAndLp(false);
	        	case "DVD"		-> new Dvd();
	        	default -> null;
            };

            
            this.assignAllMediaField(newMedia);

            // Old: Sử dụng Factory Pattern để hiển thị màn hình phù hợp
            // !!! 12-6-24 Sử dụng switch case cho đơn giản

            this.showCorrespondingMediaScreen(newMedia);

//            mediaShowData();
            mediasClearBtn();

        } catch (IllegalArgumentException e){
            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Error Message", null, e.getMessage());
            errorAlert.show();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Error Message", null, e.getMessage());
            errorAlert.show();
        }
    }
    
    private boolean missingMediaField() {
    	return media_title.getText().isEmpty()
                || media_category.getSelectionModel().getSelectedItem() == null
                || media_rushOrderSupport.getSelectionModel().getSelectedItem() == null
                || media_barcode.getText().isEmpty()
                || media_quantity.getText().isEmpty()
                || media_weight.getText().isEmpty()
                || media_price.getText().isEmpty()
                || media_productDimension.getText().isEmpty()
                || media_description.getText().isEmpty()
                || ProductManagerSession.path == null;
    }
    
    private void assignAllMediaField(Media media) {
    	media.setTitle(media_title.getText());

        String rushOrderSupportValue = media_rushOrderSupport.getSelectionModel().getSelectedItem();
        boolean rushOrderSupported = "Yes".equals(rushOrderSupportValue);
        media.setRushOrderSupported(rushOrderSupported);

        media.setBarcode(media_barcode.getText());
        media.setTotalQuantity(Integer.parseInt(media_quantity.getText()));
        media.setWeight(Double.parseDouble(media_weight.getText()));
        media.setPrice(Integer.parseInt(media_price.getText()));
        media.setProductDimension(media_productDimension.getText());
        media.setDescription(media_description.getText());

        String path = ProductManagerSession.path;
        int lastIndex = path.lastIndexOf("\\") + 1;
        String imageUrl = path.substring(lastIndex);
        media.setImageUrl(imageUrl);
    }
    
    private void showCorrespondingMediaScreen(Media media) {
        MediaScreen screen = switch (media.getMediaTypeName().toUpperCase()) {
	        case "BOOK"	-> {
	        	this.bookScreen.setMedia((Book) media);
	        	yield this.bookScreen; 
	        }
	        case "CD", "LP"	-> {
	        	this.cdAndLpScreen.setMedia((CdAndLp) media);
	        	yield this.cdAndLpScreen;
	        }
	        case "DVD" -> {
	        	this.dvdScreen.setMedia((Dvd) media);
	        	yield this.dvdScreen;
	        }
	        default -> null;
	    };
        
        if (screen != null) {
            screen.showScreen();
        }
    }

    private Media selectedMedia;
    public void mediaUpdateBtn(){
        if (this.missingMediaField()
                || selectedMedia == null || selectedMedia.getMediaId() == 0) {

            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Error Message", null, "Please fill all blank fields");
            errorAlert.show();
        }
        try{
            Media updatedMedia = selectedMedia;

            // Parse the new price from the text field
            int newPrice = Integer.parseInt(media_price.getText());

            // Check if the new price is within 30% to 150% of the current price
            if (newPrice < 0.3 * updatedMedia.getPrice() || newPrice > 1.5 * updatedMedia.getPrice()) {
                throw new IllegalArgumentException("Price must be within 30% to 150% of the current price");
            }

            // Check if the price has already been updated twice today
            LocalDate today = LocalDate.now();
            if (today.equals(lastPriceUpdateDate) && priceUpdateCount >= 2) {
                throw new IllegalArgumentException("Price can only be updated twice a day");
            }

            // If the date has changed, reset the count and update the date
            if (!today.equals(lastPriceUpdateDate)) {
                lastPriceUpdateDate = today;
                priceUpdateCount = 0;
            }

            // Update the price and increment the count
            this.assignAllMediaField(updatedMedia);
            priceUpdateCount++;

            this.showCorrespondingMediaScreen(updatedMedia);

//      mediaShowData();
            mediasClearBtn();
        } catch (IllegalArgumentException e){
            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Error Message", null, e.getMessage());
            errorAlert.show();
        } catch (Exception e){
            e.printStackTrace();
            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Error Message", null, e.getMessage());
            errorAlert.show();
        }
    }


    public void mediaDeleteBtn() {
        if (selectedMedia == null || selectedMedia.getMediaId() == 0) {
            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Error Message", null, "Please select a media to delete");
            errorAlert.show();
            return;
        }

        ConfirmationAlert confirmationAlert = new ConfirmationAlert();
        confirmationAlert.createAlert("Confirmation Message", null, "Are you sure you want to DELETE Media title: " + media_title.getText());
        confirmationAlert.show();

        if (!confirmationAlert.isConfirmed()) {
            InformationAlert cancelledAlert = new InformationAlert();
            cancelledAlert.createAlert("Cancelled", null, "Media deletion cancelled");
            cancelledAlert.show();
            return;
        }

        try {
            mediaDAO.deleteMedia(selectedMedia.getMediaId());
            mediaShowData();
            mediasClearBtn();
        } catch (SQLException e) {
            ErrorAlert sqlErrorAlert = new ErrorAlert();
            sqlErrorAlert.createAlert("SQL Error", null, "Error occurred while deleting media: " + e.getMessage());
            sqlErrorAlert.show();
            e.printStackTrace();
        }
    }

    public void mediasClearBtn(){
        media_title.setText("");
        media_category.getSelectionModel().clearSelection();
        media_rushOrderSupport.getSelectionModel().clearSelection();
        media_barcode.setText("");
        media_quantity.setText("");
        media_weight.setText("");
        media_price.setText("");
        media_productDimension.setText("");
        media_description.setText("");
        ProductManagerSession.path = "";
        selectedMedia = null;
        medias_imageView.setImage(null);
        media_category.setDisable(false);
    }

    public void selectMedia(){
    	selectedMedia = medias_tableView.getSelectionModel().getSelectedItem();
    	Media media = selectedMedia;
        int num = medias_tableView.getSelectionModel().getSelectedIndex();

        if ((num-1) < -1) {
			return;
		}

        media_title.setText(media.getTitle());
        media_barcode.setText(media.getBarcode());
        media_quantity.setText(String.valueOf(media.getTotalQuantity()));
        media_weight.setText(String.valueOf(media.getWeight()));
        media_price.setText(String.valueOf(media.getPrice()));
        media_productDimension.setText(media.getProductDimension());
        media_description.setText(media.getDescription());

        // Set the ComboBox values and disable them
        media_category.setValue(media.getMediaTypeName());
        media_category.setDisable(true);

        String rushOrderSupportValue = media.isRushOrderSupported() ? "Yes" : "No";
        media_rushOrderSupport.getSelectionModel().select(rushOrderSupportValue);
        
//        
//        media_rushOrderSupport.setValue(rushOrderSupportValue);
//        media_rushOrderSupport.setDisable(true);

        ProductManagerSession.path = media.getImageUrl();

        String path = media.getImageUrl();
        ProductManagerSession.date = String.valueOf(media.getImportDate());


        image = new Image(getClass().getResourceAsStream("/assets/images/" + path));
        medias_imageView.setImage(image);
    }

    public void mediasImportBtn() throws IOException {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*.png", "*.jpg"));
        File file = openFile.showOpenDialog(main_form.getScene().getWindow());
        if (file != null) {
            ProductManagerSession.path = file.getAbsolutePath();
            Image image = new Image(file.toURI().toString(), 150, 150, false, true);

            medias_imageView.setImage(image);
            BufferedImage bufferedImage = ImageIO.read(file);

            // Get the file extension
            String fileName = file.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);

            // Save the image with the detected file extension
            File output = new File("C:\\Users\\admin\\Desktop\\ITSS2\\ISD.ICT.20232-10\\AIMS\\src\\main\\resources\\assets\\images\\" + fileName);
            ImageIO.write(bufferedImage, fileExtension, output);

            System.out.println("Image saved successfully as " + fileExtension + "!");
        }
    }

    public void mediasCategoryList() {
        List<String> categoryL = new ArrayList<>();
        for (String data : categoryList) {
            categoryL.add(data);
        }

        ObservableList<String> listData = FXCollections.observableArrayList(categoryL);
        media_category.setItems(listData);
    }

    public void mediasRushOrderSupportList() {
        List<String> rushOrderSupportL = new ArrayList<>();
        for (String data : rushOrderSupportList) {
            rushOrderSupportL.add(data);
        }

        ObservableList<String> listData = FXCollections.observableArrayList(rushOrderSupportL);
        media_rushOrderSupport.setItems(listData);
    }

    public void mediaShowData() {
        try {
			mediaListData = FXCollections.observableArrayList(mediaDAO.getAllMedia());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        medias_col_id.setCellValueFactory(new PropertyValueFactory<>("mediaId"));
        medias_col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        medias_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        medias_col_quantity.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        medias_col_weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        medias_col_importDate.setCellValueFactory(new PropertyValueFactory<>("importDate"));
        medias_col_rushOrderSupport.setCellValueFactory(new PropertyValueFactory<>("rushOrderSupported"));
        medias_col_desciption.setCellValueFactory(new PropertyValueFactory<>("description"));
        medias_col_productDimension.setCellValueFactory(new PropertyValueFactory<>("productDimension"));
        medias_col_barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));

        medias_col_category.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getMediaTypeName()));
        
        medias_tableView.setItems(mediaListData);
    }


    public void logout() {
        try {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {

                // hide trang admin
                logout_btn.getScene().getWindow().hide();

                // Quay ve login form
                Stage stage = new Stage();
                LoginHandler loginHandler = new LoginHandler(stage, Configs.LOGIN_PATH);
                loginHandler.setScreenTitle("Login");
                loginHandler.show();
            }

        } catch (Exception e) {
            System.out.println("Uncessfully logout");
            e.printStackTrace();
        }
    }

    public void displayUsername() {
        String user = ProductManagerSession.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        username.setText(user);
        productManagerEmail.setText(ProductManagerSession.email);
    }

}
