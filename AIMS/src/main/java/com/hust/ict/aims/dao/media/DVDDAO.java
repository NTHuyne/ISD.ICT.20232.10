package com.hust.ict.aims.dao.media;

import com.hust.ict.aims.database.ConnectJDBC;
import com.hust.ict.aims.entity.media.CdAndLp;
import com.hust.ict.aims.entity.media.Dvd;
import com.hust.ict.aims.entity.media.Media;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * @author
 */
public class DVDDAO extends MediaDAO {

    @Override
    public Media getMediaById(int id) throws SQLException {
        String sql = "SELECT * FROM "+
                "DVD " +
                "INNER JOIN Media " +
                "ON Media.id = DVD.id " +
                "where Media.id = " + id + ";";
        Connection conn = null;
        // Connnect to database
        conn = ConnectJDBC.getConnection();
        // Create statement
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(sql);
        Dvd dvd = new Dvd();
        if(res.next()) {

// from media table
            String category = res.getString("category");
            int price = res.getInt("price");
            int value = res.getInt("value");
            String title = res.getString("title");
            String description = res.getString("description");
            int quantity = res.getInt("quantity");
            String barcode = res.getString("barcode");
            Date importDate = res.getDate("importDate");
            String imageUrl = res.getString("imageUrl");
            String productDimension = res.getString("productDimension");

            // from DVD table
            String dvdType = res.getString("dvdType");
            String director = res.getString("director");
            int runtime = res.getInt("runtime");
            String studio = res.getString("studio");
            String language = res.getString("language");
            String subtitles = res.getString("subtitles");
            Date releasedDate = res.getDate("releasedDate");
            String filmType = res.getString("filmType");

            dvd = new Dvd(id, category, price, value, title, description, quantity, importDate, barcode, productDimension, imageUrl,
                    dvdType, director, runtime, studio, language, subtitles, releasedDate, filmType);
        } else {
            throw new SQLException();
        }
        return dvd;
    }
}
