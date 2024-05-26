package com.hust.ict.aims.subsystem;

public interface IPayment {
	void payOrder(double amount, String orderInfo, IClient client);
}
