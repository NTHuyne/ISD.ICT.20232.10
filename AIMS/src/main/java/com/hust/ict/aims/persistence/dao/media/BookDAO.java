package com.hust.ict.aims.persistence.dao.media;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hust.ict.aims.entity.media.Book;
import com.hust.ict.aims.persistence.dao.media.temp.MediaTemplateDAO;

/**
 * @author
 */
public class BookDAO extends MediaTemplateDAO<Book> {
	
	@Override
    protected Book createItemFromResultSet(ResultSet res) throws SQLException {
    	return new Book(
    		mediaAccessDAO.createItemFromResultSet(res),
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
    protected PreparedStatement getAllStatement() throws SQLException {
        String sql = "SELECT * "
        		+ "FROM Book INNER JOIN Media "
        		+ "ON Media.media_id = Book.media_id;";

        // Create statement
        PreparedStatement stmt = connection.prepareStatement(sql);
        return stmt;
    }

    @Override
    protected PreparedStatement getByIdStatement(int cdAndLpId) throws SQLException {
        // Assuming 'connection' is your established JDBC connection
        String sql = "SELECT * FROM Book INNER JOIN Media ON Media.media_id = Book.media_id WHERE media_id = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, cdAndLpId);

        return statement;
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
    protected PreparedStatement addStatement(Book book) throws SQLException {
        // Thêm thông tin vào bảng Book
        String bookSql = "INSERT INTO Book (authors, coverType, publisher, publicationDate, pages, language, genre, media_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement bookStatement = connection.prepareStatement(bookSql, Statement.RETURN_GENERATED_KEYS);;
        this.prepareStatementFromBook(bookStatement, book);

        return bookStatement;
    }
    
    @Override 
    protected PreparedStatement updateStatement(Book book) throws SQLException {
        // Update Book table
        String bookSql = "UPDATE Book SET authors = ?, coverType = ?, publisher = ?, publicationDate = ?, pages = ?, language = ?, genre = ? WHERE media_id = ?";
        PreparedStatement bookStatement = connection.prepareStatement(bookSql);
    	this.prepareStatementFromBook(bookStatement, book);

        return bookStatement;
    }

	@Override
	protected String getDaoName() {
		return "book";
	}
}
