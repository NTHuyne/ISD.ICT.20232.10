package com.hust.ict.aims.persistence.dao.order;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.order.Order.OrderStatus;
import com.hust.ict.aims.entity.order.RushOrder;
import com.hust.ict.aims.persistence.dao.media.BookDAO;
import com.hust.ict.aims.persistence.dao.media.CDDAO;
import com.hust.ict.aims.persistence.dao.media.DVDDAO;

public class OrderDAO {
	private Map<Integer, Media> mediaMap;
	
	public OrderDAO(Collection<Media> allMedias) {
		this.mediaMap = new HashMap<>();
        for (Media media : allMedias) {
        	mediaMap.put(media.getMediaId(), media);
        }
	}
	
	public List<Order> getAll() throws SQLException {
		if (mediaMap == null) {
			throw new SQLException("mediaMap not assigned for getAll & getById operation");
		}
		
		System.out.println("Getting all orders...");
		
		List<Order> mergedList = new NormalOrderDAO(mediaMap).getAll();
		mergedList.addAll(new RushOrderDAO(mediaMap).getAll());
		return mergedList;
	}
	
	public Order getById(int orderId) throws SQLException {
		if (mediaMap == null) {
			throw new SQLException("mediaMap not assigned for getAll & getById operation");
		}
		Order order;
		
		order = new RushOrderDAO(mediaMap).getById(orderId);
		if (order == null) {
			order = new NormalOrderDAO(mediaMap).getById(orderId); 
		}
		
		return order;
		
	}
	
	public int add(Order order) throws SQLException {
        if (order instanceof RushOrder) {
            return new RushOrderDAO().add((RushOrder) order);
        } else {
            return new NormalOrderDAO().add(order);
        }
	}
	
	public void acceptOrder(Order order) throws SQLException {
		order.setStatus(OrderStatus.ACCEPTED);
		new NormalOrderDAO().update(order);
	}
	
	public void rejectOrder(Order order) throws SQLException {
		order.setStatus(OrderStatus.REJECTED);
		new NormalOrderDAO().update(order);
	}
}
