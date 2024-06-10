package com.hust.ict.aims.controller.productmanager;

import java.io.IOException;
import java.time.LocalDate;

import com.hust.ict.aims.entity.media.Dvd;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.persistence.dao.media.DVDDAO;
import com.hust.ict.aims.utils.ErrorAlert;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DVDScreen implements MediaScreen {

    private Media media;
    private DataChangedListener dataChangedListener;
    DVDDAO dvdDAO;

    public DVDScreen() {
    }

    public DVDScreen(Media media, DataChangedListener dataChangedListener, DVDDAO dvdDAO) {
        this.media = media;
        this.dataChangedListener = dataChangedListener;
        this.dvdDAO = dvdDAO;
    }

    @FXML
    private TextField dvd_type, dvd_director, dvd_studio, dvd_genre, dvd_language, dvd_subtitles, dvd_runtime;
    @FXML
    private DatePicker dvd_releasedDate;
    @FXML
    private Button addDVDBtn;
    @FXML
    private Label DVDDetailLabel;

    @Override
    public void showScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DVD.fxml"));
            loader.setControllerFactory(c -> this); // Use this instance as the controller
            Parent root = loader.load();

            if (media != null && media.getMediaId() != 0) {
                // We are editing an existing book
                addDVDBtn.setText("Update");
                DVDDetailLabel.setText("Edit DVD Detail");
                setDVDFields(); // Set fields only in edit mode
            } else {
                DVDDetailLabel.setText("Add DVD Detail");
            }

            Stage stage = new Stage();
            stage.setTitle(media.getMediaId() == 0 ? "Add DVD Details" : "Update DVD Details");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDVDFields() {
        try {
            // Assuming media.getId() returns the ID of the DVD you want to fetch
            Dvd dvd = dvdDAO.getDvdById(media.getMediaId());

            if (dvd != null) {
                dvd_director.setText(dvd.getDirector());
                dvd_language.setText(dvd.getLanguage());
                dvd_runtime.setText(String.valueOf(dvd.getRuntime()));
                dvd_studio.setText(dvd.getStudio());
                dvd_type.setText(dvd.getDvdType());
                dvd_subtitles.setText(dvd.getSubtitles());
                dvd_genre.setText(dvd.getGenre());
                if (dvd.getReleasedDate() != null) {
                    LocalDate localDate = new java.sql.Date(dvd.getReleasedDate().getTime()).toLocalDate();
                    dvd_releasedDate.setValue(localDate);
                }
            } else {
                System.out.println("No DVD found with ID: " + media.getMediaId());
                // Handle case where no DVD is found
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void addDVDBtnAction() {
        String studio = dvd_studio.getText();
        String genre = dvd_genre.getText();
        String dvdType = dvd_type.getText();
        String language = dvd_language.getText();
        String director = dvd_director.getText();
        String subtitles = dvd_subtitles.getText();
        int runtime = Integer.parseInt(dvd_runtime.getText());
        LocalDate localDate = dvd_releasedDate.getValue();

        if (studio.isEmpty()
                || genre.isEmpty()
                || dvdType.isEmpty()
                || language.isEmpty()
                || director.isEmpty()
                || subtitles.isEmpty()
                || dvd_runtime.getText().isEmpty()
                || localDate == null) {

            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Error Message", null, "Please fill all blank fields");
            errorAlert.show();
        }

        try{
            java.util.Date releaseDate = java.sql.Date.valueOf(localDate);
            Dvd newDvd = new Dvd(
                    media,
                    dvdType,
                    director,
                    runtime,
                    studio,
                    language,
                    subtitles,
                    releaseDate,
                    genre
            );

            // Check if it's a new dvd or an update
            if (media.getMediaId() == 0) {
                // It's a new dvd
                dvdDAO.addMedia(newDvd);
            } else {
                // It's an existing dvd
                dvdDAO.updateMedia(newDvd);
            }

            dataChangedListener.onDataChanged();
            Stage stage = (Stage) dvd_type.getScene().getWindow();
            stage.close();

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
