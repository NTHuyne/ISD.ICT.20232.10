package com.hust.ict.aims.subsystem.payment;

public interface IPayment {
	void payOrder(double amount, String orderInfo, IClient client);
}
