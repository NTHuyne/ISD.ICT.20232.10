package com.hust.ict.aims.dao;

import java.sql.SQLException;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import com.hust.ict.aims.entity.media.Book;
import com.hust.ict.aims.persistence.dao.media.BookDAO;
import com.hust.ict.aims.persistence.dao.media.MediaDAO;

@TestMethodOrder(OrderAnnotation.class)
public class BookDAOTest extends AbstractDAOTest<Book, BookDAO>{
	
	@Override
	public int getExistingItemId() {
		return AllDAOData.getExistingBook().getMediaId();
	}
	@Override
	public Book getExistingItem() {
		return AllDAOData.getExistingBook();
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
	public void deleteStatement(int id) throws SQLException {
		// Doesn't implement delete because that's mediaDAO responsibility
		new MediaDAO().deleteMedia(id);
	}
}
