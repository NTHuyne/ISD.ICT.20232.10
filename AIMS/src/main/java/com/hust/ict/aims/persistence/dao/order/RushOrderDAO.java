package com.hust.ict.aims.persistence.dao.order;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.order.RushOrder;
import com.hust.ict.aims.persistence.dao.TemplateDAO;

public class RushOrderDAO extends TemplateDAO<Order> {

	@Override
	public String getDaoName() {
		return "rushorder";
	}
	
	@Override
	protected String getByIdQuery(){
		return "SELECT * FROM RushOrderInfo WHERE order_id = ?;";
	}
	
    @Override
    protected RushOrder createItemFromResultSet(ResultSet res) throws SQLException {
    	return new RushOrder(
    		new OrderDAO().createItemFromResultSet(res),
    		res.getTimestamp("deliveryTime").toInstant().atZone(RushOrder.zoneId),
    		res.getString("instruction")
    	);
    }
}
