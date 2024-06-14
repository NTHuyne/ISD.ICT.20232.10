package com.hust.ict.aims.controller.productmanager;

import java.sql.SQLException;
import java.util.List;

import com.hust.ict.aims.persistence.dao.TemplateDAO;
import com.hust.ict.aims.utils.ConfirmationAlert;
import com.hust.ict.aims.utils.InformationAlert;

public class AlertDAOWrapper<T, V extends TemplateDAO<T>> {
	private V innerDAO;
	
	public AlertDAOWrapper(V innerDAO) {
		this.innerDAO = innerDAO;
	}
	
	public List<T> getAll(int id) throws SQLException {
		return innerDAO.getAll();
	}
	
	public T getById(int id) throws SQLException {
		return innerDAO.getById(id);
	}
	
	public int add(T item) throws SQLException {
        ConfirmationAlert confirmationAlert = new ConfirmationAlert();
        confirmationAlert.createAlert("Confirmation", null, "Are you sure you want to add this " + innerDAO.getDaoName() + "?");
        confirmationAlert.show();

        if (!confirmationAlert.isConfirmed()) {
            throw new SQLException("Cancel adding media");
        }
		
		int returnedId = innerDAO.add(item);
		
        InformationAlert alert = new InformationAlert();
        alert.createAlert("Information Message", null, "Successfully added " + innerDAO.getDaoName());
        alert.show();
        
        return returnedId;
	}
	
	public void update(T item) throws SQLException {
		ConfirmationAlert confirmationAlert = new ConfirmationAlert();
		confirmationAlert.createAlert("Confirmation", null, "Are you sure you want to update this media?");
		confirmationAlert.show();
		  
		if (!confirmationAlert.isConfirmed()) {
			throw new SQLException("Cancel updating media");
		}
		
		innerDAO.update(item);
        
		// TODO: Why does these not showing???
        InformationAlert alert = new InformationAlert();
        alert.createAlert("Information Message", null, "Successfully updated " + innerDAO.getDaoName());
        alert.show();
	}
	
	public void delete(int id) throws SQLException {
		innerDAO.delete(id);
		
		InformationAlert alert = new InformationAlert();
		alert.createAlert("Information Message", null, "Successfully deleted " + innerDAO.getDaoName());
		alert.show();
	}
}
