package com.hust.ict.aims.subsystem.payment.vnpay;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

import net.freeutils.httpserver.HTTPServer;
import net.freeutils.httpserver.HTTPServer.ContextHandler;
import net.freeutils.httpserver.HTTPServer.Request;
import net.freeutils.httpserver.HTTPServer.Response;
import net.freeutils.httpserver.HTTPServer.VirtualHost;

public class VNPayDisplay {
	private HTTPServer server;
	private static int chosenPort = Integer.valueOf(ReadPropertyValues.getProperty("server.port"));
	private static String chosenPath = ReadPropertyValues.getProperty("server.getpath");
	private static String serverHost = ReadPropertyValues.getProperty("server.host");
	
	private void displayURL(String url) {
        Desktop desk = Desktop.getDesktop();

        // Open URL
        try {
			desk.browse(new URI(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void configureURLReceivingServer() {
		// Initialize server on port
		// https://github.com/curtcox/JLHTTP/blob/main/faq.md
		
		server = new HTTPServer(chosenPort);
		System.out.println("Server initialized: " + serverHost + chosenPort + chosenPath);
		
		// default virtual host
		VirtualHost host = server.getVirtualHost(null);  
		host.addContext(chosenPath, new ContextHandler() {
		    public int serve(Request req, Response resp) throws IOException {
		    	System.out.println("Received response.");
		        resp.getHeaders().add("Content-Type", "text/plain");
		        resp.send(200, "Hello, World!");
		        
		        for (Map.Entry<String, String> entry : req.getParams().entrySet()) {
		            System.out.println(entry.getKey() + " = " + entry.getValue());
		        }
		        
		        server.stop();
		        System.out.println("Server terminating...");
		        return 0;
		    }
		});
		
		try {
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendPayOrder(String queryURL) {
		displayURL(queryURL);
		configureURLReceivingServer();
	}

	public void sendPayOrder(String queryURL, IBrowserDisplay browser) {
		browser.displayURL(queryURL);
		configureURLReceivingServer();
	}
}
