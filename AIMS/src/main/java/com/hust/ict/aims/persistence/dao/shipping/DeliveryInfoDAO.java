package com.hust.ict.aims.persistence.dao.shipping;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import com.hust.ict.aims.entity.shipping.DeliveryInfo;
import com.hust.ict.aims.persistence.dao.TemplateDAO;

public class DeliveryInfoDAO extends TemplateDAO<DeliveryInfo> {

    @Override
    protected PreparedStatement addStatement(DeliveryInfo info) throws SQLException {
        String sql = "INSERT INTO DeliveryInfo (name, phone, email, province, address, message)  "
        		+ "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
    	stmt.setString(1, info.getName());
        stmt.setString(2, info.getPhone());
        stmt.setString(3, info.getEmail());
        stmt.setString(4, info.getProvince());
        stmt.setString(5, info.getAddress());
        stmt.setString(6, info.getShippingInstructions());
        return stmt;
    }

	@Override
	protected String getDaoName() {
		return "deliveryInfo";
	}

    @Override
    protected PreparedStatement getAllStatement() throws SQLException {
        String sql = "SELECT * FROM DeliveryInfo;";
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }
    
    @Override
    protected PreparedStatement getByIdStatement(int transId) throws SQLException {
        String sql = "SELECT * FROM DeliveryInfo WHERE delivery_id = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, transId);
        return statement;
    }
	
    @Override
    public PreparedStatement deleteStatement(int transId) throws SQLException {
        String sql = "DELETE FROM DeliveryInfo WHERE delivery_id = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, transId);

        return statement;
    }
}
