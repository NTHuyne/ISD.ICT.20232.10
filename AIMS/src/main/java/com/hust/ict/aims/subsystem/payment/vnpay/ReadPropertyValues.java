package com.hust.ict.aims.subsystem.payment.vnpay;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class ReadPropertyValues {
	private static final String propFileName = "config.properties";
	private static Properties prop = new Properties();

	// Singleton
	private static final ReadPropertyValues instance = new ReadPropertyValues();
	
	public static synchronized String getProperty(String name) {
		return prop.getProperty(name);
	}
	
	private ReadPropertyValues() {
		System.out.println("ReadPropertyValues: Initialized Properties");
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
