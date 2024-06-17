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
import com.hust.ict.aims.entity.order.RushOrder;
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

	@Override
	protected String getByIdQuery(){
		return "SELECT * FROM OrderInfo WHERE order_id = ?;";
	}

	@Override
	public String getDaoName() {
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
        	deliveryInfo
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
		
		// Extra types of order?
		// Use interface later
		if (order instanceof RushOrder) {
			
		}
		
		return orderId;
	}

	@Override
	public Order getById(int orderId) throws SQLException {
		Order thisorder = super.getById(orderId);
		if (thisorder == null) {
			return null;
		}
		
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
	
//	public ArrayList<String> getOrderById(int id, String email) {
//		ArrayList<String> arrayList = new ArrayList<>();
//		Connection conn = null;
//		String query = "SELECT " +
//                "O.order_id, " +
//                "O.subtotal, " +
//                "O.status, " +
//                "D.name, " +
//                "D.phone, " +
//                "D.email, " +
//                "CONCAT(D.address, ', ', D.province) AS full_address, " +
//                "R.deliveryTime, " +
//                "R.instruction, " +
//				"I.totalAmount "+
//                "FROM OrderInfo O " +
//                "JOIN DeliveryInfo D ON O.delivery_id = D.delivery_id " +
//                "LEFT JOIN RushOrderInfo R ON O.order_id = R.order_id " +
//				"JOIN Invoice I ON O.order_id = I.order_id "+
//                "WHERE O.order_id = ? AND D.email = ?";
//		try {
//			conn = ConnectJDBC.getConnection();
//			PreparedStatement stmt = conn.prepareStatement(query);
//			stmt.setInt(1, id);
//			stmt.setString(2,email);
//			ResultSet res = stmt.executeQuery();
////			System.out.println(">>check res id: " + res.getInt("order_id"));
//			while (res.next()) {
//				System.out.println(">>check res id: " + res.getInt("order_id"));
//				arrayList.add(String.valueOf(res.getInt("order_id")));
//                arrayList.add(String.valueOf(res.getInt("subtotal")));
//                arrayList.add(res.getString("status"));
//                arrayList.add(res.getString("name"));
//                arrayList.add(res.getString("phone"));
//                arrayList.add(res.getString("email"));
//                arrayList.add(res.getString("full_address"));
//                if (res.getTimestamp("deliveryTime") != null) {
//                    arrayList.add(res.getTimestamp("deliveryTime").toString());
//                } else {
//                    arrayList.add("Khong co");
//                }
//                if(res.getString("instruction") == null) {
//                	arrayList.add("Khong co");
//                } else {
//                	arrayList.add(res.getString("instruction"));                	
//                }
//				arrayList.add(res.getString("totalAmount"));
//			}
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//			System.out.println("error");
//		}
//		return arrayList;
//		
//	}
//
//	public List<ArrayList<String>> getMediaInOrder(int id){
//		List<ArrayList<String>> mediaInOrder = new ArrayList<>();
//		Connection conn = null;
//		String query = "SELECT M.title, M.price, O.quantity, M.price*O.quantity as totalprice FROM media M JOIN order_media O ON M.media_id = O.media_id WHERE O.order_id = ?;";
//		try {
//			conn = ConnectJDBC.getConnection();
//			PreparedStatement stmt = conn.prepareStatement(query);
//			stmt.setInt(1, id);
//			ResultSet res = stmt.executeQuery();
//			while (res.next()) {
//				ArrayList<String> arrayList = new ArrayList<>();
//				arrayList.add(res.getString("title"));
//				arrayList.add(String.valueOf(res.getInt("price")));
//				arrayList.add(String.valueOf(res.getInt("quantity")));
//				arrayList.add(String.valueOf(res.getInt("totalprice")));
//				mediaInOrder.add(arrayList);
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//			System.out.println("error");
//		}
//		return mediaInOrder;
//	}

    @Override
    protected String deleteQuery() {
        return "DELETE FROM OrderInfo WHERE order_id = ?;";
    }
//
//	@Override
//	protected PreparedStatement getAllStatement() throws SQLException {
//		String sql = "SELECT * FROM OrderInfo;";
//		PreparedStatement statement = connection.prepareStatement(sql);
//		return statement;
//	}
}
