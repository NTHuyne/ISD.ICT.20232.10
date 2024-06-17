package com.hust.ict.aims.persistence.dao.order.temp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.persistence.dao.TemplateDAO;

public abstract class OrderTemplateDAO<T extends Order> extends TemplateDAO<T> {
	protected OrderAccessDAO orderAccessDAO = new OrderAccessDAO(connection);
	
	public OrderTemplateDAO() {
		
	}
	public OrderTemplateDAO(Map<Integer, Media> mediaMap) {
		this.orderAccessDAO.setMediaMap(mediaMap);
	}

	public List<Order> getAllOrder() throws SQLException {
        List<Order> itemlist = new ArrayList<>();

        PreparedStatement stmt = connection.prepareStatement(this.getAllQuery());
        ResultSet res = stmt.executeQuery();

        while (res.next()) {
        	Order item = this.createItemFromResultSet(res);
            itemlist.add(item);
        }

        return itemlist;
    }
	
	@Override
	protected String updateQuery() throws SQLException {
		return "UPDATE OrderInfo SET status = ? WHERE order_id = ?;";
	}

	@Override
	protected void updateParams(PreparedStatement stmt, Order item) throws SQLException {
		stmt.setString(1, item.getStatus().toString());
		stmt.setInt(2, item.getId());
	}
	
	@Override
    public int add(T order) throws SQLException {
        // Bắt đầu giao dịch
        connection.setAutoCommit(false);

        try {
        	order.setId(orderAccessDAO.add(order));
        	super.setNoReturnGeneratedKeys(true);
        	super.add(order);
            connection.commit(); // Hoàn thành giao dịch
            
            System.out.println("Successfully added " + this.getDaoName() + ": " + order);
            return order.getId();
        } catch (SQLException e) {
            connection.rollback(); // Hủy bỏ giao dịch
            throw e;
        } finally {
            connection.setAutoCommit(true); // Khôi phục auto-commit
        }
    }
	
	// Not used
	@Override
	protected String deleteQuery() throws SQLException {
		throw new SQLException("Order cannot be deleted!");
	}
}
