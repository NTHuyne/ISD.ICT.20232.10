package com.hust.ict.aims.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hust.ict.aims.persistence.database.ConnectJDBC;

// Utilizing Template Design Pattern
public abstract class TemplateDAO<T> {
    protected final Connection connection;

    public TemplateDAO() {
        this.connection = ConnectJDBC.getConnection();
    }	
	
    protected abstract String getDaoName();
    
    // Override these methods
    protected PreparedStatement getAllStatement() throws SQLException {
    	throw new SQLException("Unimplemented GetAll for " + getDaoName() +" DAO");
    }
    
	protected PreparedStatement getByIdStatement(int id) throws SQLException {
		throw new SQLException("Unimplemented GetById for " + getDaoName() +" DAO");
	}
    
    protected T createItemFromResultSet(ResultSet res) throws SQLException {
    	System.err.println("Cannot create item for " + getDaoName() +" DAO");
    	throw new SQLException("createItemFromResultSet is not implemented");
	}
    
    protected PreparedStatement addStatement(T item) throws SQLException {
    	// Important: should have Statement.RETURN_GENERATED_KEYS
    	throw new SQLException("Unimplemented Add for " + getDaoName() +" DAO");
    }
    
    protected PreparedStatement updateStatement(T item) throws SQLException {
    	throw new SQLException("Unimplemented Update for " + getDaoName() +" DAO");
	}
    
    protected PreparedStatement deleteStatement(int id) throws SQLException {
    	throw new SQLException("Unimplemented Delete for " + getDaoName() +" DAO");
    }
    
    
    // Implementations
    public List<T> getAll() throws SQLException {
        List<T> itemlist = new ArrayList<>();

        PreparedStatement stmt = this.getAllStatement();
        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            T item = this.createItemFromResultSet(res);
            itemlist.add(item);
        }

        return itemlist;
    }
    
    public T getById(int id) throws SQLException {
    	PreparedStatement statement = this.getByIdStatement(id);
    	
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return this.createItemFromResultSet(resultSet);
        } else {
        	throw new SQLException("No " + getDaoName() + " found for ID: " + id);
        }
    }

	public int add(T item) throws SQLException {
    	PreparedStatement statement = this.addStatement(item);
    	
        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating " + getDaoName() + " failed, no rows affected.");
        }

        // Lấy ID được tạo tự động
    	ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
        	System.out.println("Successfully created " + getDaoName() + ": " + item);
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("Creating " + getDaoName() + " failed, no ID obtained.");
        }
    }
	
	public void update(T item) throws SQLException {
		PreparedStatement statement = this.updateStatement(item);
		
        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Updating media failed, no rows affected.");
        }
        
        System.out.println("Successfully updated " + getDaoName() + ": " + item);
	}

	public void delete(int id) throws SQLException {
        PreparedStatement statement = this.deleteStatement(id);

        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Deleting " + getDaoName() + " failed, no rows affected.");
        } else {
            System.out.println("Successfully deleted " + getDaoName() +" with ID: " + id);
//            InformationAlert alert = new InformationAlert();
//            alert.createAlert("Information Message", null, "Successfully deleted " + getDaoName());
//            alert.show();
        }
    }
}
