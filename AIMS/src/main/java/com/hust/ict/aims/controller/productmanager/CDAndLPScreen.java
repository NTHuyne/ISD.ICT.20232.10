package com.hust.ict.aims.controller.productmanager;

import java.io.IOException;
import java.time.LocalDate;

import com.hust.ict.aims.entity.media.CdAndLp;
import com.hust.ict.aims.persistence.dao.media.CDDAO;
import com.hust.ict.aims.utils.ErrorAlert;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class CDAndLPScreen implements MediaScreen {

    private CdAndLp media;
    private DataChangedListener dataChangedListener;
    CDDAO cdAndLpDAO;

    public CDAndLPScreen(DataChangedListener dataChangedListener, CDDAO cdAndLpDAO) {
        this.dataChangedListener = dataChangedListener;
        this.cdAndLpDAO = cdAndLpDAO;
    }
    
    public void setMedia(CdAndLp media) {
		this.media = media;
	}

    @FXML
    private TextField cdAndLp_artists, cdAndLp_recordLabel, cdAndLp_trackList, cdAndLp_genre;
    @FXML
    private DatePicker cdAndLp_releaseDate;
    @FXML
    private ToggleGroup CDorLP;
    @FXML
    private RadioButton cdAndLp_isCD;
    @FXML
    private RadioButton cdAndLp_isLP;
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
                this.setDVDFields(media); // Set fields only in edit mode
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

    private void setDVDFields(CdAndLp cdAndLp) {
        cdAndLp_artists.setText(cdAndLp.getArtists());
        cdAndLp_genre.setText(cdAndLp.getGenre());
        cdAndLp_recordLabel.setText(cdAndLp.getRecordLabel());
        cdAndLp_trackList.setText(cdAndLp.getTrackList());
        if (cdAndLp.getIsCD()) {
        	cdAndLp_isCD.setSelected(true);
        } else {
        	cdAndLp_isLP.setSelected(true);
        }
        
        if (cdAndLp.getReleaseDate() != null) {
            LocalDate localDate = new java.sql.Date(cdAndLp.getReleaseDate().getTime()).toLocalDate();
            cdAndLp_releaseDate.setValue(localDate);
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
            return;
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
                cdAndLpDAO.add(newCdAndLp);
            } else {
                cdAndLpDAO.update(newCdAndLp);
            }

            dataChangedListener.onDataChanged();
            Stage stage = (Stage) cdAndLp_artists.getScene().getWindow();
            stage.hide();

        } catch (Exception e){
            e.printStackTrace();
            Stage stage = (Stage) cdAndLp_artists.getScene().getWindow();
            stage.hide();
        }

    }
}
