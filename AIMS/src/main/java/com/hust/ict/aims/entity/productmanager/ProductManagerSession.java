package com.hust.ict.aims.entity.productmanager;

public class ProductManagerSession {
    public static String username;
    public static String date;
    public static String path;
    public static String email;

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        ProductManagerSession.password = password;
    }

    private static String password;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        ProductManagerSession.username = username;
    }

    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        ProductManagerSession.date = date;
    }

    public static Integer getId() {
        return id;
    }

    public static void setId(Integer id) {
        ProductManagerSession.id = id;
    }

    private static Integer id;

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        ProductManagerSession.email = email;
    }
}
