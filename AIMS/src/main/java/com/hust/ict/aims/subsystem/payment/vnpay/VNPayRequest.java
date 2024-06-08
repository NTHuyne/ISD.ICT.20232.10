package com.hust.ict.aims.subsystem.payment.vnpay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VNPayRequest {
	private int amount;
	private String orderInfo;
//	private final String demoSecretKey = "demokeynotforproduction1234567890";
//
	public VNPayRequest(double amount, String orderInfo) {
		super();
		this.amount = (int) Math.round(amount);
		this.orderInfo = orderInfo;
	}

    private String vnp_TmnCode = "ZQKTCPOU";
    private String vnp_HashSecret = "BWJAFSUYGQNZTHZBDCHWJATSSHSPGCTO";
    private String vnp_Url = "http://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private String returnUrl = ReadPropertyValues.getProperty("server.host")
    		+ ":" + ReadPropertyValues.getProperty("server.port")
    		+ ReadPropertyValues.getProperty("server.getpath");

	public String buildQueryURL() throws UnsupportedEncodingException {

//        Random random = new Random();
//        String randomNumber = String.valueOf(random.nextInt(9999) + 1);
//

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";


        String vnp_TxnRef = getRandomNumber(8);	// TODO: Change this to order_id later?


        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", "INTCARD");		// International cards
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", returnUrl);
        vnp_Params.put("vnp_IpAddr", "123456789");

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+7"));
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = now.format(formatter2);
        String vnp_ExpireDate = now.plusMinutes(15).format(formatter2);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnp_Url + "?" + queryUrl;

		return paymentUrl;
	}

private static String getRandomNumber(int len) {
    Random rnd = new Random();
    String chars = "0123456789";
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
        sb.append(chars.charAt(rnd.nextInt(chars.length())));
    }
    return sb.toString();
}


	private String hmacSHA512(String secret, String message) {
		try {
			if (secret == null || message == null) {
				throw new NullPointerException();
			}

			final Mac hmacSHA256 = Mac.getInstance("HmacSHA512");
		    SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
		    hmacSHA256.init(secretKeySpec);
		    byte[] signatureBytes = hmacSHA256.doFinal(message.getBytes(StandardCharsets.UTF_8));

		    // Byte to Hex
			StringBuilder sb = new StringBuilder(2 * signatureBytes.length);
			for (byte b : signatureBytes) {
			    sb.append(String.format("%02x", b & 0xff));
			}
			return sb.toString();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}
}
