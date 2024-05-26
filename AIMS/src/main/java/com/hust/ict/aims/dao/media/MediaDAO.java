package com.hust.ict.aims.dao.media;

import com.hust.ict.aims.persistence.database.ConnectJDBC;
import com.hust.ict.aims.entity.media.Book;
import com.hust.ict.aims.entity.media.Media;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
public class MediaDAO {

    public List getAllMedia() throws SQLException {
        String sql = "SELECT * FROM Media";
        Connection conn = null;
        // Connnect to database
        conn = ConnectJDBC.getConnection();
        // Create statement
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(sql);
        ArrayList medium = new ArrayList<>();
        while (res.next()) {
            int id = res.getInt("id");
            String category = res.getString("category");
            int price = res.getInt("price");
            int value = res.getInt("value");
            String title = res.getString("title");
            String description = res.getString("description");
            int quantity = res.getInt("quantity");
            String barcode = res.getString("barcode");
            Date importDate = res.getDate("importDate");
            Boolean rushOrderSupport = res.getBoolean("rushOrderSupported");
            String imageUrl = res.getString("imageUrl");
            String productDimension = res.getString("productDimension");
            Media media = new Media(id, category, price, value, title, description, quantity, importDate, rushOrderSupport, barcode, productDimension, imageUrl);
            medium.add(media);
        }
        return medium;
    }

    public Media getMediaById(int id) throws SQLException {
        String sql = "SELECT * FROM Book"+
                "where id =" + id + ";";
        Connection conn = null;
        // Connnect to database
        conn = ConnectJDBC.getConnection();
        // Create statement
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(sql);

        if (res.next()) {
            String category = res.getString("category");
            int price = res.getInt("price");
            int value = res.getInt("value");
            String title = res.getString("title");
            String description = res.getString("description");
            int quantity = res.getInt("quantity");
            String barcode = res.getString("barcode");
            Date importDate = res.getDate("importDate");
            Boolean rushOrderSupport = res.getBoolean("rushOrderSupport");
            String imageUrl = res.getString("imageUrl");
            String productDimension = res.getString("productDimension");
            Media media = new Media(id, category, price, value, title, description, quantity, importDate, rushOrderSupport, barcode, productDimension, imageUrl);
            return media;
        }
        return null;
    }

}
