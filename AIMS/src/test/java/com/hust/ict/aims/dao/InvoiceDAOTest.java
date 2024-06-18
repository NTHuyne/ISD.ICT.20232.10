package com.hust.ict.aims.dao;

import java.sql.SQLException;
import java.time.Instant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.unitils.reflectionassert.ReflectionAssert;

import com.hust.ict.aims.entity.invoice.Invoice;
import com.hust.ict.aims.entity.order.Order.OrderStatus;
import com.hust.ict.aims.entity.payment.PaymentTransaction;
import com.hust.ict.aims.entity.order.RushOrder;
import com.hust.ict.aims.persistence.dao.media.MediaDAO;
import com.hust.ict.aims.persistence.dao.order.OrderDAO;
import com.hust.ict.aims.persistence.dao.order.RushOrderDAO;
import com.hust.ict.aims.persistence.dao.payment.InvoiceDAO;
import com.hust.ict.aims.utils.ObjectPrinting;

public class InvoiceDAOTest extends AbstractDAOTest<Invoice, InvoiceDAO> {
	
	@Override
	public int getExistingItemId() {
		return 0; // AllDAOData.getExistingInvoice().getId();
	}

	@Override
	public Invoice getExistingItem() {
		return null; // AllDAOData.getExistingInvoice();
	}
	
	@Override
	@Test
	@Order(2)
	public void testAdd() {
		try {
			Invoice newInvoice = new Invoice(
				AllDAOData.getExistingRushOrder()
			);
			
			newInvoice.setTransaction(new PaymentTransaction(
	        	1,
	        	Instant.now(),
	        	newInvoice.getTotalAmount(),
	        	"???",
	        	"No bank",
	        	"No bank"
	        ));
			
			this.getDAO().addFromStart(newInvoice);
			
//			ObjectPrinting.printAllAttributes(queriedItem);
//			ObjectPrinting.printAllAttributes(trueItem);
//			ObjectPrinting.checkAllAttributes(queriedItem, trueItem);
//			
//			Assertions.assertTrue(new ReflectionEquals(queriedItem).matches(trueItem));
		} catch (SQLException e) {
			e.printStackTrace();
			
			Assertions.fail();
		}
	}


	@Override
	public InvoiceDAO getDAO() {
		try {
			return new InvoiceDAO(new OrderDAO(new MediaDAO().getAllMedia()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void prepareAddItem(Invoice item) {
		item.setTotalAmount(666);
	}

	@Override
	public void prepareUpdateItem(Invoice item) {
		item.setTotalAmount(888);
	}
	
	
	@Override
	void testDelete() {
		System.out.print("NO TEST");
	}

}
