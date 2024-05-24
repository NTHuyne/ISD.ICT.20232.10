package com.hust.ict.aims.subsystem.vnpay;

import java.io.UnsupportedEncodingException;

import com.hust.ict.aims.subsystem.IClient;
import com.hust.ict.aims.subsystem.IPayment;

public class VNPayOrderManager implements IPayment {
	private IClient client;
	private VNPayDisplay vnpayDisplay;
	
	public VNPayOrderManager() {
		super();
		this.vnpayDisplay = new VNPayDisplay();
	}

	void processResponse(String url) {
		
	}

	@Override
	public void payOrder(double amount, String orderInfo, IClient client) {
		// TODO Auto-generated method stub
		this.client = client;
		Request newRequest = new Request(amount, orderInfo);
		
	
		String queryURL = "";
		try {
			 queryURL = newRequest.buildQueryURL();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(queryURL);
		vnpayDisplay.sendPayOrder(queryURL);
	}
	
    public static void main(String[] args) {
        System.out.println("MainClassB is running");
        VNPayOrderManager orderManage = new VNPayOrderManager();
        orderManage.payOrder(1806000, "Thanh toan don hang :5", null);
    }
}
