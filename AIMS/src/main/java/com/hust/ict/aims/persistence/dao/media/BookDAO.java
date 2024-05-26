package com.hust.ict.aims.persistence.dao.media;



import com.hust.ict.aims.persistence.database.ConnectJDBC;
import com.hust.ict.aims.entity.media.Book;
import com.hust.ict.aims.entity.media.Media;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * @author
 */
public class BookDAO extends MediaDAO {
    @Override
    public Media getMediaById(int id) throws SQLException {
        String sql = "SELECT * FROM "+
                "Book " +
                "INNER JOIN Media " +
                "ON Media.id = Book.id " +
                "where Media.id = " + id + ";";
        Connection conn = null;
        // Connnect to database
        conn = ConnectJDBC.getConnection();
        // Create statement
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(sql);
        Book book = new Book();
        if(res.next()) {

            // from Media table
            String category = res.getString("category");
            int price = res.getInt("price");
            int value = res.getInt("value");
            String title = res.getString("title");
            String description = res.getString("description");
            int quantity = res.getInt("quantity");
            String barcode = res.getString("barcode");
            Date importDate = res.getDate("importDate");
//            Boolean rushOrderSupport = res.getBoolean("rushOrderSupport");
            String imageUrl = res.getString("imageUrl");
            String productDimension = res.getString("productDimension");

            // from Book table
            String authors = res.getString("authors");
            String hardCover = res.getString("hardCover");
            String publisher = res.getString("publisher");
            Date publicationDate = res.getDate("publicationDate");
            int pages = res.getInt("pages");
            String language = res.getString("language");
            String bookCategory = res.getString("bookCategory");
            book = new Book(id, category, price, value, title, description, quantity, importDate, barcode, productDimension, imageUrl,
                    authors, hardCover, publisher, publicationDate, pages, language, bookCategory);
        } else {
            throw new SQLException();
        }
        return book;
    }
}
