package com.hust.ict.aims.controller.productmanager;

import com.hust.ict.aims.entity.media.CdAndLp;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.persistence.dao.media.CDDAO;
import com.hust.ict.aims.utils.ErrorAlert;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class CDAndLPScreen implements MediaScreen {

    private Media media;
    private DataChangedListener dataChangedListener;
    CDDAO cdAndLpDAO;

    public CDAndLPScreen() {
    }

    public CDAndLPScreen(Media media, DataChangedListener dataChangedListener, CDDAO cdAndLpDAO) {
        this.media = media;
        this.dataChangedListener = dataChangedListener;
        this.cdAndLpDAO = cdAndLpDAO;
    }

    @FXML
    private TextField cdAndLp_artists, cdAndLp_recordLabel, cdAndLp_trackList, cdAndLp_genre;
    @FXML
    private DatePicker cdAndLp_releaseDate;
    @FXML
    private CheckBox cdAndLp_isCD;
    @FXML
    private Button addCDAndLPBtn;
    @FXML
    private Label CDAndLPDetailLabel;

    @Override
    public void showScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CDAndLP.fxml"));
            loader.setControllerFactory(c -> this); // Use this instance as the controller
            Parent root = loader.load();

            if (media != null && media.getMediaId() != 0) {
                // We are editing an existing book
                addCDAndLPBtn.setText("Update");
                CDAndLPDetailLabel.setText("Edit CD/ LP Detail");
                setDVDFields(); // Set fields only in edit mode
            } else {
                CDAndLPDetailLabel.setText("Add CD/ LP Detail");
            }

            Stage stage = new Stage();
            stage.setTitle(media.getMediaId() == 0 ? "Add CD/ LP Details" : "Update CD/ LP Details");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDVDFields() {
        try {
            // Assuming media.getId() returns the ID of the DVD you want to fetch
            CdAndLp cdAndLp = cdAndLpDAO.getCDAndLPById(media.getMediaId());

            if (cdAndLp != null) {
                cdAndLp_artists.setText(cdAndLp.getArtists());
                cdAndLp_genre.setText(cdAndLp.getGenre());
                cdAndLp_recordLabel.setText(cdAndLp.getRecordLabel());
                cdAndLp_trackList.setText(cdAndLp.getTrackList());
                cdAndLp_isCD.setSelected(cdAndLp.getIsCD());
                if (cdAndLp.getReleaseDate() != null) {
                    LocalDate localDate = new java.sql.Date(cdAndLp.getReleaseDate().getTime()).toLocalDate();
                    cdAndLp_releaseDate.setValue(localDate);
                }
            } else {
                System.out.println("No CD/ LP found with ID: " + media.getMediaId());
                // Handle case where no DVD is found
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void addCDAndLPBtnAction() {
        String trackList = cdAndLp_trackList.getText();
        String genre = cdAndLp_genre.getText();
        String artists = cdAndLp_artists.getText();
        String recordLabel = cdAndLp_recordLabel.getText();
        LocalDate localDate = cdAndLp_releaseDate.getValue();
        Boolean isCD = cdAndLp_isCD.isSelected(); // TODO:

        if (trackList.isEmpty()
                || genre.isEmpty()
                || artists.isEmpty()
                || recordLabel.isEmpty()
                || localDate == null) {

            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Error Message", null, "Please fill all blank fields");
            errorAlert.show();
        }
		
        try{
            java.util.Date releasedDate = java.sql.Date.valueOf(localDate);
            CdAndLp newCdAndLp = new CdAndLp(
                    media,
                    artists,
                    recordLabel,
                    trackList,
                    releasedDate,
                    genre,
                    isCD
            );

            if (media.getMediaId() == 0) {
                cdAndLpDAO.addMedia(newCdAndLp);
            } else {
                cdAndLpDAO.updateMedia(newCdAndLp);
            }

            dataChangedListener.onDataChanged();
            Stage stage = (Stage) cdAndLp_artists.getScene().getWindow();
            stage.close();

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
