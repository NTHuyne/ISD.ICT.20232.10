package com.hust.ict.aims.controller;

import java.sql.SQLException;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.persistence.dao.order.OrderDAO;

public class ViewOrderController {
	 public Order getOrderById(int orderId, String email, OrderDAO orderDAO) throws SQLException {
		 System.out.println(">>check order id: " + orderId + " email: " + email);

		 	Order gottem = orderDAO.getById(orderId);
		 			
		 	if(gottem == null || !gottem.getDeliveryInfo().getEmail().equals(email)) {
		 		throw new SQLException("No " + orderDAO.getDaoName() + " found for ID: " + orderId + " and email: " + email);
		 	}
		 	
		 	return gottem;
	 }
}
