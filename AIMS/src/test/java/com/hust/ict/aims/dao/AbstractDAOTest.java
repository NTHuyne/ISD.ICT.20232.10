package com.hust.ict.aims.dao;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.junit.jupiter.api.Assertions;

import com.hust.ict.aims.persistence.dao.TemplateDAO;

@TestMethodOrder(OrderAnnotation.class)
public abstract class AbstractDAOTest<T, V extends TemplateDAO<T>> {
	public abstract T getExistingItem();
	public abstract V getDAO();
	public abstract int getItemId(T item);
	
	public abstract void prepareAddItem(T item);
	public abstract void prepareUpdateItem(T item);
	
	public String[] excludeFields() {
		return null;
	}
	
	private T tempItem = getExistingItem();		// Copy to a temp item
	
	@Test
	@Order(1)
	public void testGetById() {
		try {
			T queriedItem = this.getDAO().getById(1);
			T trueItem = this.getExistingItem();
			
			Assertions.assertTrue(new ReflectionEquals(queriedItem).matches(trueItem));
		} catch (SQLException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	}
	
	@Test
	@Order(2)
	void testAdd() {
		this.prepareAddItem(tempItem);
		
		try {
			V itemDAO = this.getDAO();
			
			int queriedId = itemDAO.add(tempItem);
			T queriedItem = itemDAO.getById(queriedId);
			
			// Except importDate because it's set as Today
			Assertions.assertTrue(new ReflectionEquals(queriedItem, this.excludeFields()).matches(tempItem));
			
			// Update with id
			tempItem = queriedItem;
		} catch (SQLException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	}
	
	@Test
	@Order(3)
	void testUpdate() {
		this.prepareUpdateItem(tempItem);
		
		try {
			V itemDAO = this.getDAO();
					
			itemDAO.update(tempItem);
			T queriedItem = itemDAO.getById(this.getItemId(tempItem));
			
			Assertions.assertTrue(new ReflectionEquals(queriedItem).matches(tempItem));
		} catch (SQLException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	}
	
	public void deleteStatement() throws SQLException {
		this.getDAO().delete(this.getItemId(tempItem));
	}
	
	@Test
	@Order(4)
	void testDelete() {
		try {
			// new MediaDAO().deleteMedia(this.getItemId(tempItem));
			deleteStatement();
					
			T queriedItem = this.getDAO().getById(this.getItemId(tempItem));
			
			Assertions.assertNull(queriedItem);
		} catch (SQLException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	}
	// Doesn't implement delete because that's mediaDAO responsibility
}
