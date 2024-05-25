package com.hust.ict.aims.subsystem.vnpay;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
	
	private boolean serverStopped = false;
	
	private void configureURLReceivingServer() {
		// Initialize server on port
		// https://github.com/curtcox/JLHTTP/blob/main/faq.md
		
		server = new HTTPServer(chosenPort);
		System.out.println("Server initialized:" + serverHost + chosenPort + chosenPath);
		
		// default virtual host
		VirtualHost host = server.getVirtualHost(null);  
		host.addContext(chosenPath, new ContextHandler() {
		    public int serve(Request req, Response resp) throws IOException {
		    	System.out.println("TESTING");
		        resp.getHeaders().add("Content-Type", "text/plain");
		        resp.send(200, "Hello, World!");
		        req.getParams();
		        
		        server.stop();
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

    static long counter = 0; 
	public void sendPayOrder(String queryURL) {
		displayURL(queryURL);
		configureURLReceivingServer();
		
//        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//
//        // Schedule a task to check the state after 5 seconds, and repeat every 5 seconds
//
//        executor.scheduleAtFixedRate(() -> {
//            if (serverStopped) {
//                System.out.println("Response received. Exiting program.");
//                executor.shutdown(); // Stop the executor
//            } else {
//                System.out.println("State is not stopped. Keeping server alive. (" + counter + ")");
//                counter += 1;
//            }
//        }, 5, 5, TimeUnit.SECONDS); // Check after 5 seconds, repeat every 5 seconds
//
//        // Wait for the executor to terminate
//        try {
//            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            // Handle interrupted exception
//            e.printStackTrace();
//        }
	}

	
}
