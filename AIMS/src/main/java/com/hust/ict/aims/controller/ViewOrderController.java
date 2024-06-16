package com.hust.ict.aims.controller;

import java.util.ArrayList;
import java.util.List;

import com.hust.ict.aims.exception.LoginAccountException;
import com.hust.ict.aims.persistence.dao.order.OrderDAO;

public class ViewOrderController {
	 public ArrayList<String> getOrderById(int orderId, String email, OrderDAO orderDAO) {
		 System.out.println(">>check order id: " + orderId + " email: " + email);
		 	ArrayList<String> arrayList = new ArrayList<>();
		 	arrayList = orderDAO.getOrderById(orderId, email);
//		 	System.out.println(">>>check array list: " + arrayList.get(0));
		 	if(arrayList.isEmpty()) {
		 		return null;
		 	}
		 	return arrayList;
	 }

	 public List<ArrayList<String>> getMediaInOrder(int orderId, OrderDAO orderDAO) {
		 System.out.println(">>check order id: " + orderId );
		 List<ArrayList<String>> arrayList = orderDAO.getMediaInOrder(orderId);
		 if(arrayList.isEmpty()) {
			 return null;
		 }
		 return arrayList;
	 }
}
