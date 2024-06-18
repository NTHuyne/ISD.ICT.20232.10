package com.hust.ict.aims.dao;

import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.unitils.reflectionassert.ReflectionAssert;

import com.hust.ict.aims.entity.order.Order.OrderStatus;
import com.hust.ict.aims.entity.order.RushOrder;
import com.hust.ict.aims.persistence.dao.order.RushOrderDAO;
import com.hust.ict.aims.utils.ObjectPrinting;

public class RushOrderDAOTest extends AbstractDAOTest<RushOrder, RushOrderDAO> {
	
	@Override
	public int getExistingItemId() {
		return AllDAOData.getExistingRushOrder().getId();
	}

	@Override
	public RushOrder getExistingItem() {
		return AllDAOData.getExistingRushOrder();
	}
	
	@Override
	@Test
	@Order(1)
	public void testGetById() {
		try {
			RushOrder queriedItem = this.getDAO().getById(this.getExistingItemId());
			RushOrder trueItem = this.getExistingItem();
			
//			ObjectPrinting.printAllAttributes(queriedItem);
//			ObjectPrinting.printAllAttributes(trueItem);
			ObjectPrinting.checkAllAttributes(queriedItem, trueItem);
			
			Assertions.assertTrue(new ReflectionEquals(queriedItem).matches(trueItem));
		} catch (SQLException e) {
			e.printStackTrace();
			
			Assertions.fail();
		}
	}
	
	@Override
	@Test
	@Order(3)
	public void testUpdate() {
		try {
			RushOrder trueItem = this.getExistingItem();
			trueItem.setStatus(OrderStatus.REJECTED);
			this.getDAO().update(trueItem);
			
//			ObjectPrinting.printAllAttributes(queriedItem);
//			ObjectPrinting.printAllAttributes(trueItem);
//			ObjectPrinting.checkAllAttributes(queriedItem, trueItem);
			

		} catch (SQLException e) {
			e.printStackTrace();
			
			Assertions.fail();
		}
	}


	@Override
	public RushOrderDAO getDAO() {
		return new RushOrderDAO(AllDAOData.getAllMediaMap());
	}

	@Override
	public void prepareAddItem(RushOrder item) {
		item.setInstruction("Rush Order add test");
	}

	@Override
	public void prepareUpdateItem(RushOrder item) {
		item.setInstruction("Rush Order update test");
	}
	
	
	@Override
	void testDelete() {
		System.out.print("NO TEST");
	}

}
