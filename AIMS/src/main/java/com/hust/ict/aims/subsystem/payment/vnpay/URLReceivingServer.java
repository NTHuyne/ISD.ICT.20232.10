package com.hust.ict.aims.subsystem.payment.vnpay;

import java.io.IOException;
import net.freeutils.httpserver.HTTPServer;
import net.freeutils.httpserver.HTTPServer.ContextHandler;
import net.freeutils.httpserver.HTTPServer.Request;
import net.freeutils.httpserver.HTTPServer.Response;
import net.freeutils.httpserver.HTTPServer.VirtualHost;

public class URLReceivingServer {
	// Singleton
	private static int chosenPort = Integer.valueOf(ReadPropertyValues.getProperty("server.port"));
	private static String chosenPath = ReadPropertyValues.getProperty("server.getpath");
	private static String serverHost = ReadPropertyValues.getProperty("server.host");
	
	private HTTPServer serverInstance;
	
	public HTTPServer build(IParamsProcessor processParams) {
		// Initialize server on port
		// https://github.com/curtcox/JLHTTP/blob/main/faq.md
	
		if (serverInstance == null) {
			serverInstance = new HTTPServer(chosenPort);
		}
		
		System.out.println("Server initialized: " + serverHost + ":" + chosenPort + chosenPath);
		
		// default virtual host
		VirtualHost host = serverInstance.getVirtualHost(null);
		
		host.addContext(chosenPath, new ContextHandler() {
		    public int serve(Request req, Response resp) throws IOException {
		        resp.getHeaders().add("Content-Type", "text/plain");
		        resp.send(200, "Transaction completed! Please go back to the app.");
		        
		        System.out.println("Received response. Processing...");
		        if (processParams != null) {
		        	processParams.processParams(req.getParams());
		        } else {
		        	System.err.println("There's no class to process params! (IParamsProcessor).");
		        }
		        
		        
		        serverInstance.stop();
		        System.out.println("Server terminating...");
		        return 0;
		    }
		});
		
		return serverInstance;
	}
}
