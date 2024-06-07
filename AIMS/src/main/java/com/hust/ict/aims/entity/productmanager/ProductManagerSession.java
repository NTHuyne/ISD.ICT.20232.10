package com.hust.ict.aims.entity.productmanager;

public class ProductManagerSession {
    public static String username;
//    public static String imageUrl;
    public static String date;
    public static String path;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        ProductManagerSession.username = username;
    }

//    public static String getImageUrl() {
//        return imageUrl;
//    }

//    public static void setImageUrl(String imageUrl) {
//        ProductManagerSession.imageUrl = imageUrl;
//    }

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

    public static Integer id;
}
