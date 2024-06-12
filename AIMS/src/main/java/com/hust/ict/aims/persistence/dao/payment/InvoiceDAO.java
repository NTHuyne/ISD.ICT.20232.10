package com.hust.ict.aims.persistence.dao.payment;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hust.ict.aims.entity.invoice.Invoice;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.payment.PaymentTransaction;
import com.hust.ict.aims.persistence.dao.TemplateDAO;
import com.hust.ict.aims.persistence.dao.order.OrderDAO;

public class InvoiceDAO extends TemplateDAO<Invoice> {
	private OrderDAO orderDAO = new OrderDAO(connection);
	private PaymentTransactionDAO transDAO = new PaymentTransactionDAO(connection);
	
	
//	@Override
//	protected PreparedStatement addQuery(Invoice invoice) throws SQLException {
//        String sql = "INSERT INTO Invoice (totalAmount, transaction_id, order_id) VALUES (?, ?, ?)";
//
//        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//        
//    	stmt.setInt(1, invoice.getTotalAmount());
//        stmt.setInt(2, invoice.getTransaction().getTransactionId());
//        stmt.setInt(3, invoice.getOrder().getId());
//        
//        return stmt;
//    }
    
    @Override
    protected Invoice createItemFromResultSet(ResultSet res) throws SQLException {
    	Order inorder = orderDAO.getById(res.getInt("order_id"));
    	PaymentTransaction intrans = transDAO.getById(res.getInt("transaction_id"));
    	
        return new Invoice(
        	res.getInt("invoice_id"),
        	res.getInt("totalAmount"),
        	inorder,
        	intrans
        );
    }
    
    
    @Override
    protected String getByIdQuery() throws SQLException {
        return "SELECT * FROM Invoice WHERE invoice_id = ?;";
    }
	
    @Override
    protected String deleteQuery() {
        return "DELETE FROM Invoice WHERE invoice_id = ?;";
    }

	@Override
	public String getDaoName() {
		return "invoice";
	}
	

	public int addFromStart(Invoice invoice) throws SQLException {
    	int orderId = orderDAO.add(invoice.getOrder());
    	int transId = transDAO.add(invoice.getTransaction());
    	
    	invoice.getOrder().setId(orderId);
    	invoice.getTransaction().setTransactionId(transId);
    	
    	return super.add(invoice);
	}
}
