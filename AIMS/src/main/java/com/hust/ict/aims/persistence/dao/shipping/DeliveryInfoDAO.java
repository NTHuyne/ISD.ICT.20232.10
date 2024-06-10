package com.hust.ict.aims.persistence.dao.shipping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.hust.ict.aims.entity.payment.PaymentTransaction;
import com.hust.ict.aims.entity.shipping.DeliveryInfo;
import com.hust.ict.aims.persistence.dao.TemplateDAO;
import com.hust.ict.aims.persistence.database.ConnectJDBC;
import com.hust.ict.aims.utils.InformationAlert;

public class DeliveryInfoDAO extends TemplateDAO<DeliveryInfo> {
    private final Connection connection;

    public DeliveryInfoDAO() {
        this.connection = ConnectJDBC.getConnection();
    }
    
    @Override
    protected PreparedStatement addStatement(DeliveryInfo trans) throws SQLException {
        String sql = "INSERT INTO DeliveryInfo (paymentTime, paymentAmount, content, bankTransactionId, cardType) "
        		+ "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement stmt = connection.prepareStatement(sql);
        
    	stmt.setTimestamp(1, Timestamp.from(trans.getPaymentTime()));
        stmt.setInt(2, trans.getPaymentAmount());
        stmt.setString(3, trans.getContent());
        stmt.setString(4, trans.getBankTransactionId());
        stmt.setString(5, trans.getCardType());
        return stmt;
    }

	@Override
	protected String getDaoName() {
		return "deliveryInfo";
	}

	@Override
	protected PreparedStatement deleteStatement(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
    

}
