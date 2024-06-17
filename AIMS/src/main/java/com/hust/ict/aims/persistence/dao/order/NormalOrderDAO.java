package com.hust.ict.aims.persistence.dao.order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.persistence.dao.order.temp.OrderTemplateDAO;

/**
 * @author
 */
public class NormalOrderDAO extends OrderTemplateDAO<Order> {
	
	public NormalOrderDAO() {}
	public NormalOrderDAO(Map<Integer, Media> mediaMap) {
		super(mediaMap);
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
	public String getDaoName() {
		// TODO Auto-generated method stub
		return "normalorder";
	}

	@Override
	protected String getAllQuery() throws SQLException {
		return "SELECT * FROM OrderInfo O "
				+ "WHERE NOT EXISTS (SELECT 1 FROM RushOrderInfo R WHERE R.order_id = O.order_id);";
//				+ "  AND NOT EXISTS (SELECT 1 FROM C WHERE C.id = O.id);";
	}

	@Override
	protected String getByIdQuery() throws SQLException {
		return "SELECT * FROM OrderInfo O "
				+ "WHERE NOT EXISTS (SELECT 1 FROM RushOrderInfo R WHERE R.order_id = O.order_id) "
				+ "AND O.order_id = ?;";
	}
	
	@Override
	protected Order createItemFromResultSet(ResultSet res) throws SQLException {
		return this.orderAccessDAO.createItemFromResultSet(res);
	}
	
	
	
	
	// Unused
	@Override
	protected String addQuery() throws SQLException {
		throw new SQLException("Normal order use add from OrderAccess");
	}
	@Override
	protected void addParams(PreparedStatement stmt, Order item) throws SQLException {
		throw new SQLException("Normal order use add from OrderAccess");
	}
}
