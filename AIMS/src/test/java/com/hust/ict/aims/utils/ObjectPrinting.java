package com.hust.ict.aims.utils;

import java.lang.reflect.Field;
import java.util.Objects;

public class ObjectPrinting {
    private static boolean belongsToJavaPackage(Class<?> clazz) {
        // Get the package of the class
        Package pkg = clazz.getPackage();
        
        // Check if the package starts with "java."
        if (pkg == null || pkg.getName().startsWith("java.")) {
            return true;
        }
        
        return false;
    }
    
    private static final int limit = 10;
    private static int currentRecurse = 0;
	// For testing only
    public static boolean checkAllAttributes(Object obj1, Object obj2) {
        if (obj1.getClass() != obj2.getClass()) {
        	System.err.println("Different class: " + obj1.getClass().toString() + " -- " + obj2.getClass().toString());
        	return false;
        }
        
        currentRecurse += 1;
        if (currentRecurse > limit) {
        	currentRecurse = 0;
        	return false;
        }
        
        Class<?> objClass = obj1.getClass();
    	boolean result = true;
    	
        do {
            Field[] fields = objClass.getDeclaredFields();
        	
	        for (Field field : fields) {
	        	try {
		            field.setAccessible(true); // to access private fields
	        	} catch (Exception e) {
	        		continue;
	        	}

	            try {
	            	System.out.println(field.getName() + " : " + field.getType().toString());
		        	
	            	// if (field.getType() )
	            	if (belongsToJavaPackage(field.getType()) || field.getType().isEnum()) {
		        		result = result && Objects.equals(field.get(obj1), field.get(obj2));
		        	} else {
		        		result = result && checkAllAttributes(field.get(obj1), field.get(obj2));
		        	}
		        	if (!result) {
		        		System.err.println(field.getName() + " not equal");
		        		return false;
		        	}
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            }
	        }
	        objClass = objClass.getSuperclass();
        } while (objClass != null);
        
        return result;
    }
}
