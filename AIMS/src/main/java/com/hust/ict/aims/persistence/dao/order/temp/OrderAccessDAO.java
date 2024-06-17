package com.hust.ict.aims.persistence.dao.order.temp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.order.OrderMedia;
import com.hust.ict.aims.entity.order.Order.OrderStatus;
import com.hust.ict.aims.entity.shipping.DeliveryInfo;
import com.hust.ict.aims.persistence.dao.TemplateDAO;
import com.hust.ict.aims.persistence.dao.shipping.DeliveryInfoDAO;

public class OrderAccessDAO extends TemplateDAO<Order> {	
	private DeliveryInfoDAO deliveryDAO = new DeliveryInfoDAO(connection);

	public OrderAccessDAO(Connection connection) {
		super(connection);
	}
	
	private Map<Integer, Media> mediaMap;
	public void setMediaMap(Map<Integer, Media> mediaMap) {
		this.mediaMap = mediaMap;
	}
	

	@Override
	public String getDaoName() {
		return "order";
	}
	
    @Override
    public Order createItemFromResultSet(ResultSet res) throws SQLException {
    	DeliveryInfo deliveryInfo = deliveryDAO.getById(res.getInt("delivery_id"));
    	int orderId = res.getInt("order_id");
    	
        return new Order(
        	orderId,
        	res.getInt("shippingFees"),
        	res.getInt("subtotal"),
        	OrderStatus.valueOf(res.getString("status")),
        	deliveryInfo,
        	this.getOrderMediasByOrderId(orderId)
        );
    }
	
	@Override
	protected String addQuery() {
		return "INSERT INTO OrderInfo (shippingFees, subtotal, status, delivery_id)"
				+ " VALUES (?, ?, ?, ?)";
	}
	@Override
	protected void addParams(PreparedStatement stmt, Order order) throws SQLException {
		stmt.setInt(1, order.getShippingFees());
		stmt.setInt(2, order.getSubtotal());
		stmt.setString(3, order.getStatus().toString());
		stmt.setInt(4, order.getDeliveryInfo().getDeliveryId());
	}
    
	// Override some basic CRUD
	@Override
	public int add(Order order) throws SQLException {
		int deliveryId = deliveryDAO.add(order.getDeliveryInfo());
		order.getDeliveryInfo().setDeliveryId(deliveryId);
		
		int orderId = super.add(order);
		order.setId(orderId);
		
		this.addOrderMedias(order);
		
		return orderId;
	}

	
	// OrderMedias
	private List<OrderMedia> getOrderMediasByOrderId(int orderId) throws SQLException {
		if (mediaMap == null) {
			throw new SQLException("mediaMap is null, cannot get OrderMedias");
		}
		
		String sql = "SELECT * FROM Order_Media WHERE order_id = ?;";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, orderId);
        ResultSet res = statement.executeQuery();

        List<OrderMedia> mediaList = new ArrayList<OrderMedia>();
        while (res.next()) {
        	mediaList.add(new OrderMedia(
        		mediaMap.get(res.getInt("media_id")),
        		res.getInt("quantity"),
        		OrderMedia.OrderType.fromInt(res.getInt("orderType"))
        	));
        }
        
        return mediaList;
	}
	
	private void addOrderMedias(Order order) throws SQLException {
		connection.setAutoCommit(false);
		String sql = "INSERT INTO Order_Media (order_id, media_id, quantity, orderType) VALUES (?, ?, ?, ?)";

		PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		for (OrderMedia orderMedia : order.getLstOrderMedia()) {
			stmt.setInt(1, order.getId());
			stmt.setInt(2, orderMedia.getMedia().getMediaId());
			stmt.setInt(3, orderMedia.getQuantity());
			stmt.setInt(4, orderMedia.getOrderType().toInt());
			stmt.addBatch();
		}
		
		try {
			stmt.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		}

		// connection.setAutoCommit(true);
	}
	
    @Override
    protected String deleteQuery() {
        return "DELETE FROM OrderInfo WHERE order_id = ?;";
    }


    // Unused
	@Override
	protected String getAllQuery() throws SQLException {
		throw new SQLException("Unimplemented GetAll for " + getDaoName() +" DAO");
	}
	@Override
	protected String getByIdQuery() throws SQLException {
		throw new SQLException("Unimplemented GetById for " + getDaoName() +" DAO");
	}
	@Override
	protected String updateQuery() throws SQLException {
		throw new SQLException("Unimplemented Update for " + getDaoName() +" DAO");
	}
	@Override
	protected void updateParams(PreparedStatement stmt, Order item) throws SQLException {
		throw new SQLException("Unimplemented Update for " + getDaoName() +" DAO");
	}
}
