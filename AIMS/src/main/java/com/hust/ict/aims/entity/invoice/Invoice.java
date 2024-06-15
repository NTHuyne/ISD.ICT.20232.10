package com.hust.ict.aims.entity.invoice;

import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.payment.PaymentTransaction;

public class Invoice {
    private int id;
    private int totalAmount;
    private Order order;
    private PaymentTransaction transaction;

    public Invoice(int id, int totalAmount, Order order, PaymentTransaction trans) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.order = order;
        this.transaction = trans;
    }

    public Invoice(Order order) {
        this.order = order;
        order.getLstOrderMedia().forEach(item -> {
            this.totalAmount += item.getMedia().getPrice();
        });
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

	public PaymentTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(PaymentTransaction transaction) {
		this.transaction = transaction;
	}
}
