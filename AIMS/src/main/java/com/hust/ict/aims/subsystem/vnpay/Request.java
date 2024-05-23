package com.hust.ict.aims.subsystem.vnpay;

import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.client.utils.URIBuilder;

public class Request {
	private double amount;
	private String orderInfo;
	
	private final String demoSecretKey = "demokeynotforproduction1234567890";
	
	public Request(double amount, String orderInfo) {
		super();
		this.amount = amount;
		this.orderInfo = orderInfo;
	}

	public String buildQueryURL() {
        Random random = new Random();
        String randomNumber = String.valueOf(random.nextInt(9999) + 1);
        
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+7"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        
        String createDate = now.format(formatter);
        String expireDate = now.plusMinutes(15).format(formatter);
        
		try {
			URIBuilder uriBuilder = 
				new URIBuilder("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html")
				.addParameter("vnp_Amount", String.valueOf(this.amount))
				.addParameter("vnp_OrderInfo", this.orderInfo)
				.addParameter("vnp_OrderType", "other")		// VNPay OrderType. Reference on website
				
				.addParameter("vnp_CreateDate", createDate)
				.addParameter("vnp_ExpireDate", expireDate)
				
				.addParameter("vnp_BankCode", "INTCARD")	// !!! VNPay payment type
				
				.addParameter("vnp_Command", " pay")		// VNPay command (in this case: pay)
				.addParameter("vnp_CurrCode", "VND")		// Currency code
				.addParameter("vnp_IpAddr", "127.0.0.1")	// Customer IP
				.addParameter("vnp_Locale", "vn")			// Language of website
				
				.addParameter("vnp_ReturnUrl", "https://domain.vn/VnPayReturn")		// Return URL after payment
				.addParameter("vnp_TmnCode", "DEMOV210")	// Website code on VNPay System???
				.addParameter("vnp_TxnRef", randomNumber)	// Different order has different code??
				.addParameter("vnp_Version", "2.1.0");
			
			String stringToHash = uriBuilder.build().toASCIIString();
			
			// Add Hash
			uriBuilder.addParameter("vnp_SecureHash", this.generateHMACSignature(stringToHash, demoSecretKey));
			
			URL url = uriBuilder.build().toURL();
			return url.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	
	
	private String generateHMACSignature(String message, String secret) throws Exception {
	    Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
	    SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
	    hmacSHA256.init(secretKeySpec);
	    byte[] signatureBytes = hmacSHA256.doFinal(message.getBytes());
	    return Base64.getEncoder().encodeToString(signatureBytes);
	}
}
