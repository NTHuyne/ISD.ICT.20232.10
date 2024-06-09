package com.hust.ict.aims.persistence.dao.media;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.persistence.database.ConnectJDBC;
import com.hust.ict.aims.utils.ConfirmationAlert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
public abstract class MediaAccessDAO {
	public abstract List<Media> getAllMedia() throws SQLException;
//	public abstract Media getMediaById(int id) throws SQLException;
    public abstract void addMedia(Media media) throws SQLException;
    public abstract void updateMedia(Media media) throws SQLException;
    
    
    protected final Connection connection;

    public MediaAccessDAO() {
        this.connection = ConnectJDBC.getConnection();
    }
    
    public Media createMediaFromResultSet(ResultSet res) throws SQLException {
    	return new Media(
        	res.getInt("media_id"),
        	res.getString("title"), 
        	res.getInt("price"), 
        	res.getInt("totalQuantity"),
        	res.getDouble("weight"),
        	res.getBoolean("rushOrderSupport"),
        	res.getString("imageUrl"),
        	res.getString("barcode"),
        	res.getString("description"),
        	res.getDate("importDate"),
        	res.getString("productDimension")
        );
    }

    	
    // Media (price, title, totalQuantity, weight, description, importDate, rushOrderSupported, imageUrl, productDimension, barcode)
    private void prepareStatementFromMedia(PreparedStatement mediaStatement, Media media) throws SQLException {
        mediaStatement.setInt(1, media.getPrice());
        mediaStatement.setString(2, media.getTitle());
        mediaStatement.setInt(3, media.getTotalQuantity());
        mediaStatement.setDouble(4, media.getWeight());
        mediaStatement.setString(5, media.getDescription());

        // Đặt ngày hiện tại cho importDate
        java.sql.Date importDate = new java.sql.Date(new Date().getTime());
        mediaStatement.setDate(6, importDate);

        boolean rushOrderSupported = (media.getRushOrderSupport() != null) ? media.getRushOrderSupport() : false;
        mediaStatement.setBoolean(7, rushOrderSupported);
        mediaStatement.setString(8, media.getImageUrl());
        mediaStatement.setString(9, media.getProductDimension());
        mediaStatement.setString(10, media.getBarcode());
    }
    
    public boolean isTitleTaken(String title) throws SQLException {
        String checkTitleSql = "SELECT title FROM Media WHERE title = ?";
        try (PreparedStatement statement = connection.prepareStatement(checkTitleSql)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
    }
    
    // Return generated key
    public int addTempMedia(Media media) throws SQLException {
        ConfirmationAlert confirmationAlert = new ConfirmationAlert();
        confirmationAlert.createAlert("Confirmation", null, "Are you sure you want to add this media?");
        confirmationAlert.show();

        if (!confirmationAlert.isConfirmed()) {
            throw new SQLException("Cancel adding media");
        }

        String mediaInsertSql = 
        	"INSERT INTO Media (price, title, totalQuantity, weight, description, importDate, rushOrderSupported, imageUrl, productDimension, barcode) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement mediaStatement = connection.prepareStatement(mediaInsertSql, Statement.RETURN_GENERATED_KEYS)) {
	        this.prepareStatementFromMedia(mediaStatement, media);
	
	        int affectedRows = mediaStatement.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("Creating media failed, no rows affected.");
	        }
	
	        // Lấy ID được tạo tự động
	    	ResultSet generatedKeys = mediaStatement.getGeneratedKeys();
	        if (generatedKeys.next()) {
	            return generatedKeys.getInt(1);
	        } else {
	            throw new SQLException("Creating CD/LP failed, no ID obtained.");
	        }
        }
    }
    
    public void updateTempMedia(Media media) throws SQLException {
        ConfirmationAlert confirmationAlert = new ConfirmationAlert();
        confirmationAlert.createAlert("Confirmation", null, "Are you sure you want to update this media?");
        confirmationAlert.show();
        
        if (!confirmationAlert.isConfirmed()) {
            throw new SQLException("Cancel updating media");
        }
        
        String mediaSql = "UPDATE Media SET price = ?, title = ?, totalQuantity = ?, weight = ?, description = ?, importDate = ?, rushOrderSupported = ?, imageUrl = ?, productDimension = ?, barcode = ? WHERE media_id = ?";
        
        try (PreparedStatement mediaStatement = connection.prepareStatement(mediaSql)) {
	        // Thiết lập các tham số cho Media
	        this.prepareStatementFromMedia(mediaStatement, media);
	        mediaStatement.setInt(11, media.getMediaId());
	
	        int affectedRows = mediaStatement.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("Updating media failed, no rows affected.");
	        }
        }
    }
}
