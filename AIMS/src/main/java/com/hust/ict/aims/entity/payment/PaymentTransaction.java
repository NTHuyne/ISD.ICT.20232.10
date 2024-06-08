package com.hust.ict.aims.entity.payment;

import java.time.Instant;

public class PaymentTransaction {
    private String transactionId;
    private String bankTransactionId;	// BankTransactionId
	private int paymentAmount;
    private Instant paymentTime;
    private String content;
    private String cardType;

	public PaymentTransaction(String transactionId, String bankTransactionId, int paymentAmount, Instant paymentTime,
			String content, String cardType) {
		super();
		this.transactionId = transactionId;
		this.bankTransactionId = bankTransactionId;
		this.paymentAmount = paymentAmount;
		this.paymentTime = paymentTime;
		this.content = content;
		this.cardType = cardType;
	}


	public PaymentTransaction() {}


    public Instant getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Instant paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}



	public String getTransactionId() {
		return transactionId;
	}



	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}


	public int getPaymentAmount() {
		return paymentAmount;
	}


	public void setPaymentAmount(int paymentAmount) {
		this.paymentAmount = paymentAmount;
	}


	public String getBankTransactionId() {
		return bankTransactionId;
	}


	public void setBankTransactionId(String bankTransactionId) {
		this.bankTransactionId = bankTransactionId;
	}
}
