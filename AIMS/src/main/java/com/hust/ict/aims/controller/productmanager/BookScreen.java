package com.hust.ict.aims.controller.productmanager;

import java.io.IOException;
import java.time.LocalDate;

import com.hust.ict.aims.entity.media.Book;
import com.hust.ict.aims.persistence.dao.media.BookDAO;
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

public class BookScreen implements MediaScreen {

    private Book media;
    private DataChangedListener dataChangedListener;
    private AlertDAOWrapper<Book, BookDAO> bookDAO;

    public BookScreen(DataChangedListener dataChangedListener, BookDAO bookDAO) {
        this.dataChangedListener = dataChangedListener;
        this.bookDAO = new AlertDAOWrapper<Book, BookDAO>(bookDAO);
    }
    
    public void setMedia(Book media) {
		this.media = media;
	}

	@FXML
    private TextField book_author, book_coverType, book_publisher, book_language, book_genre, book_pages;
    @FXML
    private DatePicker book_publicationDate;
    @FXML
    private Button addBookBtn;
    @FXML
    private Label bookDetailLabel;

    @Override
    public void showScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/book.fxml"));
            loader.setControllerFactory(c -> this); // Use this instance as the controller
            Parent root = loader.load();

            if (media != null && media.getMediaId() != 0) {
                // We are editing an existing book
                addBookBtn.setText("Update");
                bookDetailLabel.setText("Edit Book Detail");
                this.setBookFields(media); // Set fields only in edit mode
            } else {
                bookDetailLabel.setText("Add Book Detail");
            }

            Stage stage = new Stage();
            stage.setTitle(media.getMediaId() == 0 ? "Add Book Details" : "Update Book Details");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setBookFields(Book book) {
        book_author.setText(book.getAuthors());
        book_coverType.setText(book.getCoverType());
        book_publisher.setText(book.getPublisher());
        book_language.setText(book.getLanguage());
        book_genre.setText(book.getGenre());
        book_pages.setText(String.valueOf(book.getPages()));
        if (book.getPublicationDate() != null) {
            LocalDate localDate = new java.sql.Date(book.getPublicationDate().getTime()).toLocalDate();
            book_publicationDate.setValue(localDate);
        }
    }

    @FXML
    private void addBookBtnAction() {
        String author = book_author.getText();
        String coverType = book_coverType.getText();
        String publisher = book_publisher.getText();
        String language = book_language.getText();
        String genre = book_genre.getText();
        int pages = Integer.parseInt(book_pages.getText());
        LocalDate localDate = book_publicationDate.getValue();

        if (author.isEmpty()
                || coverType.isEmpty()
                || publisher.isEmpty()
                || language.isEmpty()
                || genre.isEmpty()
                || book_pages.getText().isEmpty()
                || localDate == null) {

            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Error Message", null, "Please fill all blank fields");
            errorAlert.show();
            return;
        }

        try{
            java.util.Date publicationDate = java.sql.Date.valueOf(localDate);

            Book newBook = new Book(
                    media,
                    author,
                    coverType,
                    publisher,
                    publicationDate,
                    pages,
                    language,
                    genre
            );

            // Check if it's a new book or an update
            if (media.getMediaId() == 0) {
                // It's a new book
                bookDAO.add(newBook);
            } else {
                // It's an existing book
            	bookDAO.update(newBook);
            }

            dataChangedListener.onDataChanged();
            Stage stage = (Stage) book_author.getScene().getWindow();
            stage.hide();

        } catch (Exception e){
            e.printStackTrace();
            Stage stage = (Stage) book_author.getScene().getWindow();
            stage.hide();
        }

    }
}
