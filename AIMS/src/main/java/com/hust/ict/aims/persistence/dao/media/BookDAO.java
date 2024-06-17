package com.hust.ict.aims.persistence.dao.media;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	protected String getAllQuery() {
		return "SELECT * " + "FROM Book INNER JOIN Media " + "ON Media.media_id = Book.media_id;";

	}

	@Override
	protected String getByIdQuery() throws SQLException {
		return "SELECT * FROM Book INNER JOIN Media ON Media.media_id = Book.media_id WHERE Media.media_id = ?;";
	}

	// Book (authors, coverType, publisher, publicationDate, pages, language, genre,
	// media_id)
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
	protected String addQuery() {
		return "INSERT INTO Book (authors, coverType, publisher, publicationDate, pages, language, genre, media_id)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	}
	@Override
	protected void addParams(PreparedStatement bookStatement, Book book) throws SQLException {
		// Thêm thông tin vào bảng Book
		this.prepareStatementFromBook(bookStatement, book);
	}

	@Override
	protected String updateQuery() {
		return "UPDATE Book SET authors = ?, coverType = ?, publisher = ?, publicationDate = ?, pages = ?, language = ?, genre = ? WHERE media_id = ?";
	}
	@Override
	protected void updateParams(PreparedStatement bookStatement, Book book) throws SQLException {
		// Update Book table
		this.prepareStatementFromBook(bookStatement, book);
	}

	@Override
	public String getDaoName() {
		return "book";
	}
	
	
	// Not used
	@Override
	protected String deleteQuery() throws SQLException {
		throw new SQLException("Unimplemented delete for " + getDaoName() +" DAO");
	}
}
