package com.hust.ict.aims.persistence.dao.payment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.hust.ict.aims.entity.payment.PaymentTransaction;
import com.hust.ict.aims.persistence.dao.TemplateDAO;

public class PaymentTransactionDAO extends TemplateDAO<PaymentTransaction> {
	@Override
	protected PreparedStatement addStatement(PaymentTransaction trans) throws SQLException {
        String sql = "INSERT INTO PaymentTransaction (paymentTime, paymentAmount, content, bankTransactionId, cardType) "
        		+ "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
    	stmt.setTimestamp(1, Timestamp.from(trans.getPaymentTime()));
        stmt.setInt(2, trans.getPaymentAmount());
        stmt.setString(3, trans.getContent());
        stmt.setString(4, trans.getBankTransactionId());
        stmt.setString(5, trans.getCardType());
        
        return stmt;
    }
    
    @Override
    protected PaymentTransaction createItemFromResultSet(ResultSet res) throws SQLException {
        return new PaymentTransaction(
        	res.getInt("transaction_id"),
        	res.getTimestamp("paymentTime").toInstant(),
        	res.getInt("paymentAmount"),
        	res.getString("content"),
        	res.getString("bankTransactionId"),
        	res.getString("cardType")
        );
    }
    
    @Override
    protected PreparedStatement getAllStatement() throws SQLException {
        String sql = "SELECT * FROM PaymentTransaction;";
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }
    
    @Override
    protected PreparedStatement getByIdStatement(int transId) throws SQLException {
        String sql = "SELECT * FROM PaymentTransaction WHERE transaction_id = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, transId);
        return statement;
    }
	
    @Override
    protected PreparedStatement deleteStatement(int transId) throws SQLException {
        String sql = "DELETE FROM PaymentTransaction WHERE transaction_id = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, transId);

        return statement;
    }

	@Override
	protected String getDaoName() {
		return "transaction";
	}
}
