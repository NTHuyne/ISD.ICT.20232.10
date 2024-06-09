package com.hust.ict.aims.persistence.dao.media;

import com.hust.ict.aims.utils.ErrorAlert;
import com.hust.ict.aims.utils.InformationAlert;
import com.hust.ict.aims.entity.media.Book;
import com.hust.ict.aims.entity.media.Media;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public class BookDAO extends MediaAccessDAO {
    private Book createBookFromResultSet(ResultSet res) throws SQLException {
    	return new Book(
			createMediaFromResultSet(res), 
        	res.getString("authors"),
        	res.getString("coverType"),
        	res.getString("publisher"),
        	res.getDate("publicationDate"),
        	res.getInt("pages"),
        	res.getString("language"),
        	res.getString("genre")
        );
    }
    
    @Override
    public List<Media> getAllMedia() throws SQLException {
        List<Media> medialist = new ArrayList<Media>();
        
        String sql = "SELECT * "
        		+ "FROM Book INNER JOIN Media "
        		+ "ON Media.media_id = Book.media_id;";
        
        // Create statement
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(sql);
        
        while (res.next()) {
            Book book = this.createBookFromResultSet(res);
            
            medialist.add(book);
        }
        
        return medialist;
    }

    public Book getBookById(int cdAndLpId) {
        // Assuming 'connection' is your established JDBC connection
        String sql = "SELECT * FROM Book INNER JOIN Media ON Media.media_id = Book.media_id WHERE media_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cdAndLpId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return this.createBookFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching DVD");
            e.printStackTrace();
        }
        return null;
    }
    
    
    
    // Book (authors, coverType, publisher, publicationDate, pages, language, genre, media_id)
    private void prepareStatementFromBook(PreparedStatement bookStatement, Book book) throws SQLException {
        bookStatement.setString(1, book.getAuthors());
        bookStatement.setString(2, book.getCoverType());
        bookStatement.setString(3, book.getPublisher());

        if (book.getPublicationDate() != null) {
            bookStatement.setDate(4, new java.sql.Date(book.getPublicationDate().getTime()));
        } else {
            bookStatement.setNull(4, java.sql.Types.DATE);
        }

        bookStatement.setInt(5, book.getPages());
        bookStatement.setString(6, book.getLanguage());
        bookStatement.setString(7, book.getGenre());
        bookStatement.setInt(8, book.getMediaId());
    }
    
    @Override
    public void addMedia(Media media) throws SQLException {
        if (isTitleTaken(media.getTitle())) {
            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Error Message", null, "Media title is already taken");
            errorAlert.show();
            throw new IllegalArgumentException(media.getTitle() + " is already taken");
        }

        // Bắt đầu giao dịch
        connection.setAutoCommit(false);

        try {
            // Xử lý thêm Book, bao gồm thông tin của Media
            Book book = (Book) media;

            book.setMediaId(this.addTempMedia(media));

            // Thêm thông tin vào bảng Book
            String bookSql = "INSERT INTO Book (authors, coverType, publisher, publicationDate, pages, language, genre, media_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement bookStatement = connection.prepareStatement(bookSql)) {
                this.prepareStatementFromBook(bookStatement, book);

                bookStatement.executeUpdate();
            }
            
            System.out.println("Successfully added book: " + media);

            InformationAlert alert = new InformationAlert();
            alert.createAlert("Information Message", null, "Successfully added book" );
            alert.show();

            connection.commit(); // Hoàn thành giao dịch
        } catch (SQLException e) {
            connection.rollback(); // Hủy bỏ giao dịch
            throw e;
        } finally {
            connection.setAutoCommit(true); // Khôi phục auto-commit
        }
    }
    
    @Override
    public void updateMedia(Media media) throws SQLException {
        // Start transaction
        connection.setAutoCommit(false);

        try {
            Book book = (Book) media;

            // Update Media table
            this.updateTempMedia(media);
            
            // Update Book table
            String bookSql = "UPDATE Book SET authors = ?, coverType = ?, publisher = ?, publicationDate = ?, pages = ?, language = ?, genre = ? WHERE media_id = ?";
            try (PreparedStatement bookStatement = connection.prepareStatement(bookSql)) {
            	this.prepareStatementFromBook(bookStatement, book);

                bookStatement.executeUpdate();
            }

            System.out.println("Successfully updated book: " + media);
            InformationAlert alert = new InformationAlert();
            alert.createAlert("Information Message", null, "Successfully updated book" );
            alert.show();

            connection.commit(); // Commit transaction
        } catch (SQLException e) {
            connection.rollback(); // Roll back transaction
            throw e;
        } finally {
            connection.setAutoCommit(true); // Restore auto-commit
//            System.out.println("Updating media with ID: " + media.getId());
        }
    }
}
