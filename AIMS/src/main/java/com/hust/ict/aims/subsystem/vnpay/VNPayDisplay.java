package com.hust.ict.aims.subsystem.vnpay;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException; 

public class VNPayDisplay {
	private void displayURL(String url) {
        Desktop desk = Desktop.getDesktop(); 
        
        // Open URL
        try {
			desk.browse(new URI(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendPayOrder(String queryURL) {
		displayURL(queryURL);
	}
}
