package com.hust.ict.aims.persistence.dao.order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.entity.order.RushOrder;
import com.hust.ict.aims.persistence.dao.order.temp.OrderTemplateDAO;

public class RushOrderDAO extends OrderTemplateDAO<RushOrder> {

	public RushOrderDAO() {}
	public RushOrderDAO(Map<Integer, Media> mediaMap) {
		super(mediaMap);
	}

	

	
	@Override
	public String getDaoName() {
		return "rushorder";
	}

	
	@Override
	protected String getAllQuery(){
		return "SELECT * FROM RushOrderInfo R JOIN OrderInfo O ON R.order_id = O.order_id;";
	}
	
	@Override
	protected String getByIdQuery(){
		return "SELECT * FROM RushOrderInfo R JOIN OrderInfo O ON R.order_id = O.order_id WHERE O.order_id = ?;";
	}
	
    @Override
    protected RushOrder createItemFromResultSet(ResultSet res) throws SQLException {
    	return new RushOrder(
    		this.orderAccessDAO.createItemFromResultSet(res),
    		res.getTimestamp("deliveryTime").toInstant(),
    		res.getString("instruction")
    	);
    }

    
	
	
	@Override
	protected String addQuery() {
		return "INSERT INTO RushOrderInfo (deliveryTime, instruction, order_id) VALUES (?, ?, ?)";
	}
	@Override
	protected void addParams(PreparedStatement stmt, RushOrder order) throws SQLException {
		stmt.setTimestamp(1, Timestamp.from(order.getDeliveryTime()));
		stmt.setString(2, order.getInstruction());
		stmt.setInt(3, order.getId());
	}
}
