package com.hust.ict.aims.entity.payment;

import java.time.Instant;

public class PaymentTransaction {
    private int transactionId;
    private Instant paymentTime;
	private int paymentAmount;
    private String content;
    
    private String bankTransactionId;	// BankTransactionId
    private String cardType;
    
	public PaymentTransaction(int transactionId, Instant paymentTime, int paymentAmount, String content,
			String bankTransactionId, String cardType) {
		super();
		this.transactionId = transactionId;
		this.paymentTime = paymentTime;
		this.paymentAmount = paymentAmount;
		this.content = content;
		this.bankTransactionId = bankTransactionId;
		this.cardType = cardType;
	}
	
	public PaymentTransaction() {}
	
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public Instant getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(Instant paymentTime) {
		this.paymentTime = paymentTime;
	}
	public int getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(int paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getBankTransactionId() {
		return bankTransactionId;
	}
	public void setBankTransactionId(String bankTransactionId) {
		this.bankTransactionId = bankTransactionId;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

}
