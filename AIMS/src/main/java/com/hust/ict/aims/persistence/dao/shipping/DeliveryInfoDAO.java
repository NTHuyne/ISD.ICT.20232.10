package com.hust.ict.aims.persistence.dao.shipping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hust.ict.aims.entity.shipping.DeliveryInfo;
import com.hust.ict.aims.persistence.dao.TemplateDAO;

public class DeliveryInfoDAO extends TemplateDAO<DeliveryInfo> {
	public DeliveryInfoDAO(Connection conn) {
		super(conn);
	}
	
    @Override
    protected String addQuery() {
    	return "INSERT INTO DeliveryInfo (name, phone, email, province, address, message)  "
        		+ "VALUES (?, ?, ?, ?, ?, ?)";
    }
    @Override
    protected void addParams(PreparedStatement stmt, DeliveryInfo info) throws SQLException {
    	stmt.setString(1, info.getName());
        stmt.setString(2, info.getPhone());
        stmt.setString(3, info.getEmail());
        stmt.setString(4, info.getProvince());
        stmt.setString(5, info.getAddress());
        stmt.setString(6, info.getShippingInstructions());
    }

	@Override
	public String getDaoName() {
		return "deliveryInfo";
	}
	
	@Override
    protected DeliveryInfo createItemFromResultSet(ResultSet res) throws SQLException {
    	return new DeliveryInfo(
    		res.getInt("delivery_id"),
        	res.getString("name"),
        	res.getString("phone"),
        	res.getString("email"),
        	res.getString("province"),
        	res.getString("address"),
        	res.getString("message")
        );
    }

    @Override
    protected String getAllQuery() {
        return "SELECT * FROM DeliveryInfo;";
    }
    
    @Override
    protected String getByIdQuery() {
        return "SELECT * FROM DeliveryInfo WHERE delivery_id = ?;";
    }
	
    @Override
    public String deleteQuery() {
        return "DELETE FROM DeliveryInfo WHERE delivery_id = ?;";
    }
    
    
	// Unused
	// Unused
	@Override
	protected String updateQuery() throws SQLException {
		throw new SQLException("Unimplemented Update for " + getDaoName() +" DAO");
	}
	@Override
	protected void updateParams(PreparedStatement stmt, DeliveryInfo item) throws SQLException {
		throw new SQLException("Unimplemented Update for " + getDaoName() +" DAO");
	}
}
