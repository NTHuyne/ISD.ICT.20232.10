package com.hust.ict.aims.dao;

import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import com.hust.ict.aims.entity.media.Book;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.persistence.dao.media.BookDAO;
import com.hust.ict.aims.persistence.dao.media.MediaDAO;

@TestMethodOrder(OrderAnnotation.class)
public class BookDAOTest extends AbstractDAOTest<Book, BookDAO>{
	@Override
	public int getExistingItemId() {
		return 1;
	}
	@Override
	public Book getExistingItem() {
		Media trueMedia = new Media(
				this.getExistingItemId(),
	        	"Sample Book",
	        	200000,
	        	100,
	        	1.0,
	        	true,
	        	"sample_book.jpg",
	        	"12345",
	        	"This is a sample book",
	        	java.sql.Date.valueOf(LocalDate.parse("2022-01-01")),
	        	"12x6x9"
			);
					
			return new Book(
				trueMedia,
	        	"Author Name",
	        	"Hardcover",
	        	"Publisher Name",
	        	java.sql.Date.valueOf(LocalDate.parse("2022-01-01")),
	        	300,
	        	"English",
	        	"Fiction"
	        );
	}

	@Override
	public BookDAO getDAO() {
		return new BookDAO();
	}

	@Override
	public void prepareAddItem(Book item) {
		item.setTitle("ADDING");
		item.setPages(999);
	}

	@Override
	public void prepareUpdateItem(Book item) {
		item.setTitle("UPDATING");
		item.setPages(0);
	}
	
	@Override
	public String[] excludeFieldsForAdd() {
		String[] exclude = {"importDate"};
		return exclude;
	}
	
	@Override
	public void deleteStatement(int id) throws SQLException {
		// Doesn't implement delete because that's mediaDAO responsibility
		new MediaDAO().deleteMedia(id);
	}
}
