package com.hust.ict.aims.dao;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.junit.jupiter.api.Assertions;

import com.hust.ict.aims.persistence.dao.TemplateDAO;
import com.hust.ict.aims.utils.ObjectPrinting;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public abstract class AbstractDAOTest<T, V extends TemplateDAO<T>> {
	public abstract T getExistingItem();
	public abstract int getExistingItemId();
	public abstract V getDAO();
	
	public abstract void prepareAddItem(T item);
	public abstract void prepareUpdateItem(T item);
	
	// Overridable
	public String[] excludeFieldsForAdd() {
		return null;
	}

	private T tempItem = getExistingItem(); 		// Copy to a temp item
	private int tempItemId;
	
	@Test
	@Order(1)
	public void testGetById() {
		try {
			T queriedItem = this.getDAO().getById(this.getExistingItemId());
			T trueItem = this.getExistingItem();
			
			ObjectPrinting.printAllAttributes(queriedItem);
			ObjectPrinting.printAllAttributes(trueItem);
			
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
			
			tempItemId = itemDAO.add(tempItem);
			T queriedItem = itemDAO.getById(tempItemId);
			
			// Except importDate because it's set as Today
			Assertions.assertTrue(new ReflectionEquals(queriedItem, this.excludeFieldsForAdd()).matches(tempItem));
			
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
			T queriedItem = itemDAO.getById(tempItemId);
			
			Assertions.assertTrue(new ReflectionEquals(queriedItem).matches(tempItem));
		} catch (SQLException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	}
	
	// Overridable
	public void deleteStatement(int id) throws SQLException {
		this.getDAO().delete(tempItemId);
	}
	
	@Test
	@Order(4)
	void testDelete() {
		try {
			deleteStatement(tempItemId);
					
			T queriedItem = this.getDAO().getById(tempItemId);
			
			Assertions.assertNull(queriedItem);
		} catch (SQLException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	}

}
