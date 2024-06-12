package com.hust.ict.aims.dao;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.junit.jupiter.api.Assertions;

import com.hust.ict.aims.entity.media.Book;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.persistence.dao.media.BookDAO;
import com.hust.ict.aims.persistence.dao.media.MediaDAO;

@TestMethodOrder(OrderAnnotation.class)
public class BookDAOTest2 {
	private Book getFirstBook() {
		Media trueMedia = new Media(
        	1,
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
	
	private Book tempBook = getFirstBook();
	
	@Test
	@Order(1)
	void testGetById() {
		try {
			Book queriedBook = new BookDAO().getById(1);
			Book trueBook = getFirstBook();
			
			Assertions.assertTrue(new ReflectionEquals(queriedBook).matches(trueBook));
		} catch (SQLException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	}
	
    public static void printAllAttributes(Object obj) {
        Class<?> objClass = obj.getClass();

        do {
            Field[] fields = objClass.getDeclaredFields();
        	
	        System.out.println(obj);
	        for (Field field : fields) {
	            field.setAccessible(true); // to access private fields
	            try {
	                System.out.println(field.getName() + ": " + field.get(obj));
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            }
	        }
	        objClass = objClass.getSuperclass();
        } while (objClass != null);
    }
	
	@Test
	@Order(2)
	void testAdd() {
		tempBook.setTitle("ADDING");
		tempBook.setPages(999);
		
		try {
			BookDAO bookDAO = new BookDAO();
			
			int bookId = bookDAO.add(tempBook);
			Book queriedBook = bookDAO.getById(bookId);
			
			// Except importDate because it's set as Today
			Assertions.assertTrue(new ReflectionEquals(queriedBook, "importDate").matches(tempBook));
			
			tempBook = queriedBook;
		} catch (SQLException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	}
	
	@Test
	@Order(3)
	void testUpdate() {
		tempBook.setTitle("UPDATING");
		tempBook.setPages(0);
		
		try {
			BookDAO bookDAO = new BookDAO();
					
			bookDAO.update(tempBook);
			Book queriedBook = bookDAO.getById(tempBook.getMediaId());
			
			Assertions.assertTrue(new ReflectionEquals(queriedBook).matches(tempBook));
		} catch (SQLException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	}
	
	@Test
	@Order(4)
	void testDelete() {
		try {
			new MediaDAO().deleteMedia(tempBook.getMediaId());;
					
			Book queriedBook = new BookDAO().getById(tempBook.getMediaId());
			
			Assertions.assertNull(queriedBook);
		} catch (SQLException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	}
	// Doesn't implement delete because that's mediaDAO responsibility
}
