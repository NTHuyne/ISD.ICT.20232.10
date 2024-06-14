package com.hust.ict.aims.persistence.dao.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.order.Order.OrderStatus;
import com.hust.ict.aims.entity.order.OrderMedia;
import com.hust.ict.aims.entity.shipping.DeliveryInfo;
import com.hust.ict.aims.persistence.dao.TemplateDAO;
import com.hust.ict.aims.persistence.dao.shipping.DeliveryInfoDAO;
import com.hust.ict.aims.persistence.database.ConnectJDBC;

/**
 * @author
 */
public class OrderDAO extends TemplateDAO<Order> {
	private DeliveryInfoDAO deliveryDAO = new DeliveryInfoDAO(connection);
	
	public OrderDAO(Connection conn) {
		super(conn);
	}
	
	private Map<Integer, Media> mediaMap;
	public OrderDAO() {
		
	}
	public OrderDAO(Collection<Media> allMedias) {
		super();
		
		this.mediaMap = new HashMap<>();
        for (Media media : allMedias) {
        	mediaMap.put(media.getMediaId(), media);
        }
	}

	// INSERT INTO OrderInfo (shippingFees, subtotal, status, delivery_id) VALUES
	// (100, 500, 1, 1);
	@Override
	protected PreparedStatement addStatement(Order order) throws SQLException {
		String sql = "INSERT INTO OrderInfo (shippingFees, subtotal, status, delivery_id) VALUES (?, ?, ?, ?);";

		PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		stmt.setInt(1, order.getShippingFees());
		stmt.setInt(2, order.getSubtotal());
		stmt.setString(3, order.getStatus().toString());
		stmt.setInt(4, order.getDeliveryInfo().getDeliveryId());

		return stmt;
	}

	@Override
	protected PreparedStatement getByIdStatement(int orderId) throws SQLException {
		String sql = "SELECT * FROM OrderInfo WHERE order_id = ?;";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, orderId);
		return statement;
	}

	@Override
	protected String getDaoName() {
		return "order";
	}
	
    @Override
    protected Order createItemFromResultSet(ResultSet res) throws SQLException {
    	DeliveryInfo deliveryInfo = deliveryDAO.getById(res.getInt("delivery_id"));
    	
        return new Order(
        	res.getInt("order_id"),
        	res.getInt("shippingFees"),
        	res.getInt("subtotal"),
        	OrderStatus.valueOf(res.getString("status")),
        	deliveryInfo,
        	false,
        	null,
        	null
        );
    }
	
	// Override some basic CRUD
	@Override
	public int add(Order order) throws SQLException {
		int deliveryId = deliveryDAO.add(order.getDeliveryInfo());
		order.getDeliveryInfo().setDeliveryId(deliveryId);
		
		int orderId = super.add(order);
		
		connection.setAutoCommit(false);
		
		String sql = "INSERT INTO Order_Media (order_id, media_id, quantity) VALUES (?, ?, ?)";

		PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		for (OrderMedia orderMedia : order.getLstOrderMedia()) {
			stmt.setInt(1, orderId);
			stmt.setInt(2, orderMedia.getMedia().getMediaId());
			stmt.setInt(3, orderMedia.getQuantity());
			stmt.addBatch();
		}
		
		try {
			stmt.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		}

		connection.setAutoCommit(true);
		
		return orderId;
	}

	@Override
	public Order getById(int orderId) throws SQLException {
		Order thisorder = super.getById(orderId);
		
		String sql = "SELECT * FROM Order_Media WHERE order_id = ?;";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, orderId);
        ResultSet res = statement.executeQuery();

        List<OrderMedia> mediaList = new ArrayList<OrderMedia>();
        while (res.next()) {
        	mediaList.add(new OrderMedia(
        		mediaMap.get(res.getInt("media_id")),
        		res.getInt("quantity")
        	));
        }
        
        thisorder.setLstOrderMedia(mediaList);

		return thisorder;
	}
	
	public ArrayList<String> getOrderById(int id, String email) {
		ArrayList<String> arrayList = new ArrayList<>();
		Connection conn = null;
		String query = "SELECT " +
                "O.order_id, " +
                "O.subtotal, " +
                "O.status, " +
                "D.name, " +
                "D.phone, " +
                "D.email, " +
                "CONCAT(D.address, ', ', D.province) AS full_address, " +
                "R.deliveryTime, " +
                "R.instruction " +
                "FROM OrderInfo O " +
                "JOIN DeliveryInfo D ON O.delivery_id = D.delivery_id " +
                "LEFT JOIN RushOrderInfo R ON O.order_id = R.order_id " +
                "WHERE O.order_id = ? AND D.email = ?";
		try {
			conn = ConnectJDBC.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			stmt.setString(2,email);
			ResultSet res = stmt.executeQuery();
//			System.out.println(">>check res id: " + res.getInt("order_id"));
			while (res.next()) {
				System.out.println(">>check res id: " + res.getInt("order_id"));
				arrayList.add(String.valueOf(res.getInt("order_id")));
                arrayList.add(String.valueOf(res.getInt("subtotal")));
                arrayList.add(res.getString("status"));
                arrayList.add(res.getString("name"));
                arrayList.add(res.getString("phone"));
                arrayList.add(res.getString("email"));
                arrayList.add(res.getString("full_address"));
                if (res.getTimestamp("deliveryTime") != null) {
                    arrayList.add(res.getTimestamp("deliveryTime").toString());
                } else {
                    arrayList.add("Khong co");
                }
                if(res.getString("instruction") == null) {
                	arrayList.add("Khong co");
                } else {
                	arrayList.add(res.getString("instruction"));                	
                }
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("errror");
		}
		return arrayList;
		
	}

//    @Override
//    protected PreparedStatement deleteStatement(int orderId) throws SQLException {
//        String sql = "DELETE FROM OrderInfo WHERE order_id = ?;";
//        PreparedStatement statement = connection.prepareStatement(sql);
//        statement.setInt(1, orderId);
//
//        return statement;
//    }
//
//	@Override
//	protected PreparedStatement getAllStatement() throws SQLException {
//		String sql = "SELECT * FROM OrderInfo;";
//		PreparedStatement statement = connection.prepareStatement(sql);
//		return statement;
//	}
}
