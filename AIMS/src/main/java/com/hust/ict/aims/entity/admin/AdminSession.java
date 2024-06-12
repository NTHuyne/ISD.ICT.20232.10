package com.hust.ict.aims.entity.admin;

public class AdminSession {
    public static String username;
    public static String email;
    public static Integer id;
    public static Boolean isAdmin;
    public static String password;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        AdminSession.username = username;
    }

    public static Integer getId() {
        return id;
    }

    public static void setId(Integer id) {
        AdminSession.id = id;
    }

    public static Boolean getIsAdmin() { return isAdmin; }

    public static void setIsAdmin(Boolean isAdmin) { AdminSession.isAdmin = isAdmin; }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        AdminSession.email = email;
    }

    public static String getPassword() { return password; }

    public static void setPassword(String password) { AdminSession.password = password; }
}
