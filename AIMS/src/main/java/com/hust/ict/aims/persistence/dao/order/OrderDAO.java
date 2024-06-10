package com.hust.ict.aims.persistence.dao.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.payment.PaymentTransaction;
import com.hust.ict.aims.entity.shipping.DeliveryInfo;
import com.hust.ict.aims.persistence.dao.TemplateDAO;
import com.hust.ict.aims.persistence.database.ConnectJDBC;
import com.hust.ict.aims.utils.InformationAlert;

/**
 * @author
 */
public class OrderDAO extends TemplateDAO<Order> {
	// INSERT INTO OrderInfo (shippingFees, subtotal, status, delivery_id) VALUES
	// (100, 500, 1, 1);
	@Override
	protected PreparedStatement addStatement(Order order) throws SQLException {
		String sql = "INSERT INTO OrderInfo (shippingFees, subtotal, status, delivery_id) VALUES (?, ?, ?, ?);";

		PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		stmt.setInt(1, order.getShippingFees());
		stmt.setInt(2, order.getShippingFees());
		stmt.setString(3, order.getStatus().toString());
		stmt.setInt(4, order.getDeliveryInfo().getDeliveryId());

		return stmt;
	}

	@Override
	protected String getDaoName() {
		return "order";
	}

	@Override
	protected PreparedStatement getAllStatement() throws SQLException {
		String sql = "SELECT * FROM OrderInfo;";
		PreparedStatement statement = connection.prepareStatement(sql);
		return statement;
	}

	@Override
	protected PreparedStatement getByIdStatement(int orderId) throws SQLException {
		String sql = "SELECT * FROM OrderInfo WHERE order_id = ?;";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, orderId);
		return statement;
	}

	@Override
	public Order getById(int orderId) throws SQLException {
		super.getById(orderId);
		return null;
	}

//    @Override
//    protected PreparedStatement deleteStatement(int orderId) throws SQLException {
//        String sql = "DELETE FROM OrderInfo WHERE order_id = ?;";
//        PreparedStatement statement = connection.prepareStatement(sql);
//        statement.setInt(1, orderId);
//
//        return statement;
//    }

}
