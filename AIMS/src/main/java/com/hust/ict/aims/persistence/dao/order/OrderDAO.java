package com.hust.ict.aims.persistence.dao.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.payment.PaymentTransaction;
import com.hust.ict.aims.persistence.database.ConnectJDBC;
import com.hust.ict.aims.utils.InformationAlert;

/**
 * @author
 */
public class OrderDAO {
    private final Connection connection;

    public OrderDAO() {
        this.connection = ConnectJDBC.getConnection();
    }
    
    public void add(Order order) throws SQLException {
        String sql = "INSERT INTO PaymentTransaction (paymentTime, paymentAmount, content, bankTransactionId, cardType) "
        		+ "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement stmt = connection.prepareStatement(sql);
        
//    	stmt.setTimestamp(1, Timestamp.from(trans.getPaymentTime()));
//        stmt.setInt(2, trans.getPaymentAmount());
//        stmt.setString(3, trans.getContent());
//        stmt.setString(4, trans.getBankTransactionId());
//        stmt.setString(5, trans.getCardType());
        stmt.executeUpdate();
    }
    
    public Order getById(int orderId) throws SQLException {
        String sql = "SELECT * FROM OrderInfo WHERE order_id = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, orderId);

        ResultSet res = statement.executeQuery();
        if (res.next()) {
            return new PaymentTransaction(
            	res.getInt("transaction_id"),
            	res.getTimestamp(sql).toInstant(),
            	res.getInt(sql),
            	res.getString(sql),
            	res.getString(transId),
            	res.getString(sql)
            );
        } else {
        	throw new SQLException("No transaction found for ID: " + transId);
        }
    }
	
    public void delete(int transId) throws SQLException {
        String sql = "DELETE FROM PaymentTransaction WHERE transaction_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, transId);

        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Deleting transaction failed, no rows affected.");
        } else {
            System.out.println("Successfully deleted transaction with ID: " + transId);
            InformationAlert alert = new InformationAlert();
            alert.createAlert("Information Message", null, "Successfully deleted transaction");
            alert.show();
        }
    }
	
	
//    public List<Media> getOrder(int id) throws SQLException {
//        Connection conn = null;
//        try {
//            String sql = "SELECT Media.title, Order_Media.Quantity\n" +
//                    "FROM Order_Media\n" +
//                    "LEFT JOIN Media\n" +
//                    "ON Order_Media.media_id = Media.media_id\n" +
//                    "where orderId =" + id + ";";
//
//            conn = ConnectJDBC.getConnection();
//            Statement stmt = conn.createStatement();
//            // Get data
//            ResultSet rs = stmt.executeQuery(sql);
//
//            int i = 1;
//            int sum = 0;
//            List<Media> medium = new ArrayList<>();
//            while (rs.next()) {
//                Media media = new Media(i,
//                        rs.getString("title"),
//                        rs.getInt("price"),
//                        rs.getString("category"),
//                        rs.getInt("quantity"));
//                i++;
//                medium.add(media);
//            }
//            return medium;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
