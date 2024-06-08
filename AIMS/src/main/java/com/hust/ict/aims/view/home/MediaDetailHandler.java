package com.hust.ict.aims.view.home;

import com.hust.ict.aims.controller.HomeController;
import com.hust.ict.aims.persistence.dao.media.BookDAO;
import com.hust.ict.aims.entity.media.Book;
import com.hust.ict.aims.entity.media.CdAndLp;
import com.hust.ict.aims.entity.media.Dvd;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.utils.Utils;
import com.hust.ict.aims.view.BaseScreenHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MediaDetailHandler extends BaseScreenHandler {
    private static final Logger LOGGER = Utils.getLogger(MediaDetailHandler.class.getName());

    @FXML
    private ImageView detailedMediaImage;

    @FXML
    private Label detailedMediaTitle;

    @FXML
    private Label detailedMediaPrice;

    @FXML
    private Label detailedMediaCategory;

    @FXML
    private Label detailedMediaDescription;

    @FXML
    private Label detailedMediaRushOrder;

    @FXML
    private Label info1;

    @FXML
    private Label info2;

    @FXML
    private Label info3;

    @FXML
    private Label info4;

    @FXML
    private Label detailedInfo1;

    @FXML
    private Label detailedInfo2;

    @FXML
    private Label detailedInfo3;

    @FXML
    private Label detailedInfo4;

    @Override
    public void show() {
        super.show();
    }

    public MediaDetailHandler(Stage stage, String screenPath, Media media) throws IOException {
        super(stage, screenPath);
        try {
            setMediaDetails(media);
        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
        }
    }

    public void setMediaDetails(Media media) throws SQLException {
        detailedMediaTitle.setText(media.getTitle());
        detailedMediaPrice.setText(Utils.getCurrencyFormat(media.getPrice()));
        detailedMediaDescription.setText(media.getDescription());
        detailedMediaCategory.setText(media.getCategory());
        String rushOrder;
        if (media.getRushOrderSupport() == true) {
            rushOrder = "Yes";
        } else {
            rushOrder = "No";
        }
        detailedMediaRushOrder.setText(rushOrder);
        if (media instanceof Book) {
            info1.setText("Authors");
            info2.setText("Publisher");
            info3.setText("Pages");
            info4.setText("Language");
            Book media1 = (Book) media;
            detailedInfo1.setText(media1.getAuthors());
            detailedInfo2.setText(media1.getPublisher());
            detailedInfo3.setText(String.valueOf(media1.getPages()));
            detailedInfo4.setText(media1.getLanguage());
        }

        if (media instanceof Dvd) {
            info1.setText("Dvd Type");
            info2.setText("Director");
            info3.setText("Studio");
            info4.setText("Language");
            Dvd media1 = (Dvd) media;
            detailedInfo1.setText(media1.getDvdType());
            detailedInfo2.setText(media1.getDirector());
            detailedInfo3.setText(media1.getStudio());
            detailedInfo4.setText(media1.getLanguage());
        }

        if (media instanceof CdAndLp) {
            info1.setText("Artists");
            info2.setText("Record Label");
            info3.setText("Track List");
            info4.setText("Music Type");
            CdAndLp media1 = (CdAndLp) media;
            detailedInfo1.setText(media1.getArtists());
            detailedInfo2.setText(media1.getRecordLabel());
            detailedInfo3.setText(media1.getTrackList());
            detailedInfo4.setText(media1.getMusicType());
        }
    }

    private void loadImage(String imageUrl) {
        File file = new File(imageUrl);
        Image image = new Image(file.toURI().toString());
        detailedMediaImage.setImage(image);
    }
}
