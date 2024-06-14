package com.hust.ict.aims.controller;

import java.util.ArrayList;
import com.hust.ict.aims.exception.LoginAccountException;
import com.hust.ict.aims.persistence.dao.order.OrderDAO;

public class ViewOrderController {
	 public ArrayList<String> getOrderById(int orderId, String email, OrderDAO orderDAO) throws LoginAccountException {
		 System.out.println(">>check order id: " + orderId + " email: " + email);
		 	ArrayList<String> arrayList = new ArrayList<>();
		 	arrayList = orderDAO.getOrderById(orderId, email);
//		 	System.out.println(">>>check array list: " + arrayList.get(0));
		 	if(arrayList.isEmpty()) {
		 		return null;
		 	}
		 	return arrayList;
	    }
}
