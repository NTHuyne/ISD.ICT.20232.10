package com.hust.ict.aims.persistence.dao.payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.hust.ict.aims.entity.payment.PaymentTransaction;
import com.hust.ict.aims.persistence.dao.TemplateDAO;

// This class do not need update
public class PaymentTransactionDAO extends TemplateDAO<PaymentTransaction> {
	public PaymentTransactionDAO(Connection conn) {
		super(conn);
	}

	@Override
	protected String addQuery() {
		return "INSERT INTO PaymentTransaction (paymentTime, paymentAmount, content, bankTransactionId, cardType) "
        		+ "VALUES (?, ?, ?, ?, ?)";
	}
	
	@Override
	protected void addParams(PreparedStatement stmt, PaymentTransaction trans) throws SQLException {
    	stmt.setTimestamp(1, Timestamp.from(trans.getPaymentTime()));
        stmt.setInt(2, trans.getPaymentAmount());
        stmt.setString(3, trans.getContent());
        stmt.setString(4, trans.getBankTransactionId());
        stmt.setString(5, trans.getCardType());
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
    protected String getAllQuery() {
        return "SELECT * FROM PaymentTransaction;";
    }
    
    @Override
    protected String getByIdQuery() {
        return "SELECT * FROM PaymentTransaction WHERE transaction_id = ?;";
    }
	
    @Override
    protected String deleteQuery()  {
        return "DELETE FROM PaymentTransaction WHERE transaction_id = ?;";
    }

	@Override
	public String getDaoName() {
		return "transaction";
	}
}
