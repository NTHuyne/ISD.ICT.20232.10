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
    
    public TemplateDAO(Connection conn) {
    	this.connection = conn;
    }
	
    public abstract String getDaoName();
    protected abstract String getAllQuery() throws SQLException;
    protected abstract String getByIdQuery() throws SQLException;
    protected abstract String deleteQuery() throws SQLException;
    
	// For getAll and getById
    protected abstract T createItemFromResultSet(ResultSet res) throws SQLException;
    
    protected abstract String addQuery() throws SQLException;
    protected abstract void addParams(PreparedStatement stmt, T item) throws SQLException;
    
    protected abstract String updateQuery() throws SQLException;
    protected abstract void updateParams(PreparedStatement stmt, T item) throws SQLException;
    
    
    // Implementations
    public List<T> getAll() throws SQLException {
        List<T> itemlist = new ArrayList<>();

        PreparedStatement stmt = connection.prepareStatement(this.getAllQuery());
        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            T item = this.createItemFromResultSet(res);
            itemlist.add(item);
        }

        return itemlist;
    }
    
    public T getById(int id) throws SQLException {
    	PreparedStatement statement = connection.prepareStatement(this.getByIdQuery());
        statement.setInt(1, id);
    	
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return this.createItemFromResultSet(resultSet);
        } else {
        	return null; // throw new SQLException("No " + getDaoName() + " found for ID: " + id);
        }
    }
    
    private boolean noReturnGeneratedKeys = false;
	public void setNoReturnGeneratedKeys(boolean noReturnGeneratedKeys) {
		this.noReturnGeneratedKeys = noReturnGeneratedKeys;
	}

	public int add(T item) throws SQLException {
    	PreparedStatement statement = connection.prepareStatement(this.addQuery(), PreparedStatement.RETURN_GENERATED_KEYS);
    	this.addParams(statement, item);
    	
        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating " + getDaoName() + " failed, no rows affected.");
        }

        if (noReturnGeneratedKeys) {
        	System.out.println("Successfully created " + getDaoName() + ": " + item);
        	return -1;
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
		PreparedStatement statement = connection.prepareStatement(this.updateQuery());
    	this.updateParams(statement, item);
    	
        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Updating media failed, no rows affected.");
        }
        
        System.out.println("Successfully updated " + getDaoName() + ": " + item);
	}

	public void delete(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(this.deleteQuery());
        statement.setInt(1, id);
        
        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Deleting " + getDaoName() + " failed, no rows affected.");
        } else {
            System.out.println("Successfully deleted " + getDaoName() +" with ID: " + id);
        }
    }
}
