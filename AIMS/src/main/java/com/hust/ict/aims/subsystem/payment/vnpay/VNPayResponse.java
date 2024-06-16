package com.hust.ict.aims.subsystem.payment.vnpay;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.hust.ict.aims.entity.payment.PaymentTransaction;
import com.hust.ict.aims.exception.PaymentException;

public class VNPayResponse {
	private PaymentTransaction currentTrans;
	private DateTimeFormatter vnpayDateFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.of("GMT+7"));
	
	public PaymentTransaction parseParams(Map<String, String> params) throws PaymentException {
        currentTrans = new PaymentTransaction();
        
        for (Map.Entry<String, String> entry : params.entrySet()) {
        	this.handleParams(entry.getKey(), entry.getValue());
        }
        
		return currentTrans;
	}

	private void handleParams(String key, String value) throws PaymentException {
		switch (key) {
			case "vnp_TransactionNo": // = 14449369
				// currentTrans.setTransactionId(value);
				break;
			case "vnp_Amount":  //= 17000000
				currentTrans.setPaymentAmount(Integer.parseInt(value) / 100);
				break;
			case "vnp_BankTranNo": //= 7178171996076149003010
				currentTrans.setBankTransactionId(value); 
				break;
			case "vnp_CardType":  //= MASTERCARD
				currentTrans.setCardType(value);
				break;
			case "vnp_OrderInfo":  //= Thanh toan don hang :596957451
				currentTrans.setContent(value);
				break;
			case "vnp_PayDate":
				currentTrans.setPaymentTime(
					ZonedDateTime.parse(value, vnpayDateFormat).toInstant()
				);
				
				DateTimeFormatter debugformat = DateTimeFormatter
						.ofPattern("dd/MM/yyyy HH:mm:ss")
                        .withZone(ZoneId.systemDefault());
				System.out.println("Received vnp_PayDate=" + value);
				System.out.println("Formatted Instant: " + debugformat.format(currentTrans.getPaymentTime()));
				break; //= 20240608102602


			case "vnp_BankCode":  //= MASTERCARD
				// TODO: do something with this?
				break;

			case "vnp_ResponseCode":
				this.handleErrorCode(value);
				break; //= 00
			case "vnp_TransactionStatus":
				if (!value.equals("00")) {
					System.out.println("TransactionStatus not success (" + value + ")");
				}
				break; // = 00
				
				// TODO: Check the hash again?
			case "vnp_TmnCode":
				break; //= ZQKTCPOU
			case "vnp_TxnRef":
				break; // = 96957451
			case "vnp_SecureHash":
				break; // = 1d79957b911423a7639e3ef4419f766d77914bef7d3cf1330f89dc19fd36f010e647d6fe28a817a673c09cafecbd7546186d5e6bfa6d40681834fed14fb8d3fc
			default:
				System.err.println("Unrecognized param: " + key);
				break;
		}
	}
	
	private void handleErrorCode(String responseCode) throws PaymentException {
		switch (responseCode) {
			case "00":
				break;
			case "07": throw new PaymentException("Fraudulent transaction");
			case "09": throw new PaymentException("InternetBanking not registered");
			case "10": throw new PaymentException("Entered invalid card information 3 times");
			case "11": throw new PaymentException("Transaction took too long");
			case "13": throw new PaymentException("Incorrect OTP entered");
			case "24": throw new PaymentException("Transaction cancelled");
			
			default: throw new PaymentException("Transaction error? (" + responseCode + ")");
		}
	}
}
