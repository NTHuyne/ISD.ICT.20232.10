package com.hust.ict.aims.subsystem.vnpay;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import net.freeutils.httpserver.HTTPServer;

public class VNPayDisplay {
	private VNPayOrderManager orderManager;

	public VNPayDisplay(VNPayOrderManager orderManager) {
		super();
		this.orderManager = orderManager;
	}

	private void displayURL(String url) {
        Desktop desk = Desktop.getDesktop();

        // Open URL
        try {
			desk.browse(new URI(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void startURLReceivingServer() {
		final HTTPServer server = new URLReceivingServer().build(orderManager);

		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPayOrder(String queryURL) {
		displayURL(queryURL);
		startURLReceivingServer();
	}

	public void sendPayOrder(String queryURL, IBrowserDisplay browser) {
		browser.displayURL(queryURL);
		startURLReceivingServer();
	}
}
