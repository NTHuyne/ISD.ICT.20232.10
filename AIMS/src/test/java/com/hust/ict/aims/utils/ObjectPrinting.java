package com.hust.ict.aims.utils;

import java.lang.reflect.Field;

public class ObjectPrinting {
	private static void printAttributesOnce(Object obj) {
        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();
        
        for (Field field : fields) {
        	try {
	            field.setAccessible(true); // to access private fields
        	} catch (Exception e) {
        		continue;
        	}

            try {
                System.out.println(field.getName() + ": " + field.get(obj).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
	}
	// For testing only
    public static void printAllAttributes(Object obj) {
        Class<?> objClass = obj.getClass();

        do {
            Field[] fields = objClass.getDeclaredFields();
        	
	        System.out.println(obj);
	        for (Field field : fields) {
	        	try {
		            field.setAccessible(true); // to access private fields
	        	} catch (Exception e) {
	        		continue;
	        	}

	            try {
	                System.out.println(field.getName() + ": " + field.get(obj).toString());
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            }
	        }
	        objClass = objClass.getSuperclass();
        } while (objClass != null);
    }
}
