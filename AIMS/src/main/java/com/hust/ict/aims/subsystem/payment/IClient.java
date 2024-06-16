package com.hust.ict.aims.subsystem.payment;

import com.hust.ict.aims.entity.payment.PaymentTransaction;
import com.hust.ict.aims.exception.PaymentException;

public interface IClient {
	void updateTransactionOnFailure(PaymentException exception);
	void updateTransactionOnSuccess(PaymentTransaction trans);
}
