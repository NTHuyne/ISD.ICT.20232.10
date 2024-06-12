package com.hust.ict.aims.utils;

import java.lang.reflect.Field;

public class ObjectPrinting {
	// For testing only
    public static void printAllAttributes(Object obj) {
        Class<?> objClass = obj.getClass();

        do {
            Field[] fields = objClass.getDeclaredFields();
        	
	        System.out.println(obj);
	        for (Field field : fields) {
	            field.setAccessible(true); // to access private fields
	            try {
	                System.out.println(field.getName() + ": " + field.get(obj));
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            }
	        }
	        objClass = objClass.getSuperclass();
        } while (objClass != null);
    }
}
