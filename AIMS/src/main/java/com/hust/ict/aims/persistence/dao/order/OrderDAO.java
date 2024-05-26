package com.hust.ict.aims.persistence.dao.order;




import com.hust.ict.aims.persistence.database.ConnectJDBC;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.entity.order.Order;


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
public class OrderDAO {

    public List<Media> getOrder(int id) throws SQLException {
        Connection conn = null;
        try {
            String sql = "select  Media.title, Order_Media.price, Media.category, Order_Media.Quantity\n" +
                    "from Order_Media\n" +
                    "LEFT JOIN media\n" +
                    "on Order_Media.mediaId = media.id\n" +
                    "where orderId =" + id + ";";
            conn = ConnectJDBC.getConnection();
            Statement stmt = conn.createStatement();
            // Get data
            ResultSet rs = stmt.executeQuery(sql);

            int i = 1;
            int sum = 0;
            List<Media> medium = new ArrayList<>();
            while (rs.next()) {
                Media media = new Media(i,
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("category"),
                        rs.getInt("quantity"));
                i++;
                medium.add(media);
            }
            return medium;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
