package com.hust.ict.aims.controller.productmanager;

import java.sql.SQLException;

import com.hust.ict.aims.persistence.dao.TemplateDAO;
import com.hust.ict.aims.utils.ConfirmationAlert;
import com.hust.ict.aims.utils.InformationAlert;

public class AlertDAOWrapper<T, V extends TemplateDAO<T>> {
	private V innerDAO;
	
	public AlertDAOWrapper(V innerDAO) {
		this.innerDAO = innerDAO;
	}
	
	public void getAll(int id) throws SQLException {
		innerDAO.getAll();
	}
	
	public void getById(int id) throws SQLException {
		innerDAO.getById(id);
	}
	
	public void add(T item) throws SQLException {
        ConfirmationAlert confirmationAlert = new ConfirmationAlert();
        confirmationAlert.createAlert("Confirmation", null, "Are you sure you want to add this " + innerDAO.getDaoName() + "?");
        confirmationAlert.show();

        if (!confirmationAlert.isConfirmed()) {
            throw new SQLException("Cancel adding media");
        }
		
		innerDAO.add(item);
		
        InformationAlert alert = new InformationAlert();
        alert.createAlert("Information Message", null, "Successfully added " + innerDAO.getDaoName());
        alert.show();
	}
	
	public void update(T item) throws SQLException {
		ConfirmationAlert confirmationAlert = new ConfirmationAlert();
		confirmationAlert.createAlert("Confirmation", null, "Are you sure you want to update this media?");
		confirmationAlert.show();
		  
		if (!confirmationAlert.isConfirmed()) {
			throw new SQLException("Cancel updating media");
		}
		
		innerDAO.update(item);
		
        
        InformationAlert alert = new InformationAlert();
        alert.createAlert("Information Message", null, "Successfully updated " + innerDAO.getDaoName());
        alert.show();
	}
}
