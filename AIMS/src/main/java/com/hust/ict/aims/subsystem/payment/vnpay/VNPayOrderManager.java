package com.hust.ict.aims.subsystem.payment.vnpay;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

import com.hust.ict.aims.entity.payment.PaymentTransaction;
import com.hust.ict.aims.exception.PaymentException;
import com.hust.ict.aims.persistence.dao.payment.PaymentTransactionDAO;
import com.hust.ict.aims.subsystem.payment.IClient;
import com.hust.ict.aims.subsystem.payment.IPayment;

public class VNPayOrderManager implements IPayment, IParamsProcessor {
	private IClient client;
	private VNPayDisplay vnpayDisplay;

	public VNPayOrderManager() {
		super();
		this.vnpayDisplay = new VNPayDisplay(this);
	}

	@Override
	public void processParams(Map<String, String> params) {
		VNPayResponse newResponse = new VNPayResponse();
		PaymentTransaction trans = null;
		try {
			trans = newResponse.parseParams(params);

			client.updateTransactionOnSuccess(trans);
		} catch (PaymentException e) {
			System.err.println(e.getMessage());
			client.updateTransactionOnFailure(e);
		}
	}

	@Override
	public void payOrder(double amount, String orderInfo, IClient client) {
		// TODO Auto-generated method stub
		this.client = client;
		VNPayRequest newRequest = new VNPayRequest(amount, orderInfo);

		try {
			String queryURL = newRequest.buildQueryURL();
			System.out.println(queryURL);
			vnpayDisplay.sendPayOrder(queryURL);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public static void main(String[] args) {
        System.out.println("VNPayOrderManager is running");
        VNPayOrderManager orderManage = new VNPayOrderManager();
        orderManage.payOrder(1806000, "Thanh toan don hang :5", null);
        System.out.println("\nContinuing in VNPayOrderManager.");
    }
}
