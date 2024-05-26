package com.hust.ict.aims.persistence.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectJDBC {

    public static Connection getConnection() {
        Connection conn = null;

        try (FileInputStream f = new FileInputStream("src/main/java/db.properties")) {
            // load the properties file
            Properties pros = new Properties();
            pros.load(f);
            // assign db parameters
            String url = pros.getProperty("url");
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            // create a connection to the database
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successful !");
        } catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}

