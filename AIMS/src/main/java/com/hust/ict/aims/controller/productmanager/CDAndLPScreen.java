package com.hust.ict.aims.controller.productmanager;

import com.hust.ict.aims.entity.media.CdAndLp;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.service.productmanager.CDAndLPService;
import com.hust.ict.aims.service.productmanager.MediaService;
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
    CDAndLPService cdAndLpService;

    public CDAndLPScreen() {
    }

    public CDAndLPScreen(Media media, DataChangedListener dataChangedListener, CDAndLPService cdAndLpService) {
        this.media = media;
        this.dataChangedListener = dataChangedListener;
        this.cdAndLpService = cdAndLpService;
    }

    @FXML
    private TextField cdAndLp_artists, cdAndLp_recordLabel, cdAndLp_trackList, cdAndLp_musicType;
    @FXML
    private DatePicker cdAndLp_releaseDate;
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

            if (media != null && media.getId() != 0) {
                // We are editing an existing book
                addCDAndLPBtn.setText("Update");
                CDAndLPDetailLabel.setText("Edit CD/ LP Detail");
                setDVDFields(); // Set fields only in edit mode
            } else {
                CDAndLPDetailLabel.setText("Add CD/ LP Detail");
            }

            Stage stage = new Stage();
            stage.setTitle(media.getId() == 0 ? "Add CD/ LP Details" : "Update CD/ LP Details");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDVDFields() {
        try {
            // Assuming media.getId() returns the ID of the DVD you want to fetch
            CdAndLp cdAndLp = cdAndLpService.fetchCDAndLPFromDatabase(media.getId());

            if (cdAndLp != null) {
                cdAndLp_artists.setText(cdAndLp.getArtists());
                cdAndLp_musicType.setText(cdAndLp.getMusicType());
                cdAndLp_recordLabel.setText(cdAndLp.getRecordLabel());
                cdAndLp_trackList.setText(cdAndLp.getTrackList());
                if (cdAndLp.getReleaseDate() != null) {
                    LocalDate localDate = new java.sql.Date(cdAndLp.getReleaseDate().getTime()).toLocalDate();
                    cdAndLp_releaseDate.setValue(localDate);
                }
            } else {
                System.out.println("No CD/ LP found with ID: " + media.getId());
                // Handle case where no DVD is found
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void addCDAndLPBtnAction() {
        String trackList = cdAndLp_trackList.getText();
        String musicType = cdAndLp_musicType.getText();
        String artists = cdAndLp_artists.getText();
        String recordLabel = cdAndLp_recordLabel.getText();
        LocalDate localDate = cdAndLp_releaseDate.getValue();

        if (trackList.isEmpty()
                || musicType.isEmpty()
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
                    trackList,
                    musicType,
                    artists,
                    recordLabel,
                    releasedDate
            );

            if (media.getId() == 0) {
                cdAndLpService.addMedia(newCdAndLp);
            } else {
                cdAndLpService.updateMedia(newCdAndLp);
            }

            dataChangedListener.onDataChanged();
            Stage stage = (Stage) cdAndLp_artists.getScene().getWindow();
            stage.close();

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
