package com.hust.ict.aims.persistence.dao.media.temp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.persistence.dao.TemplateDAO;
import com.hust.ict.aims.utils.ConfirmationAlert;

/**
 * @author
 */
public class MediaAccessDAO extends TemplateDAO<Media> {
	public MediaAccessDAO(Connection conn) {
		super(conn);
	}

	
	@Override
    public Media createItemFromResultSet(ResultSet res) throws SQLException {
    	return new Media(
        	res.getInt("media_id"),
        	res.getString("title"),
        	res.getInt("price"),
        	res.getInt("totalQuantity"),
        	res.getDouble("weight"),
        	res.getBoolean("rushOrderSupported"),
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

        boolean rushOrderSupported = (media.isRushOrderSupported() != null) ? media.isRushOrderSupported() : false;
        mediaStatement.setBoolean(7, rushOrderSupported);
        mediaStatement.setString(8, media.getImageUrl());
        mediaStatement.setString(9, media.getProductDimension());
        mediaStatement.setString(10, media.getBarcode());
    }

    @Override
    protected String addQuery() {
    	return "INSERT INTO Media (price, title, totalQuantity, weight, description, importDate, rushOrderSupported, imageUrl, productDimension, barcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }
    @Override
    protected void addParams(PreparedStatement stmt, Media media) throws SQLException {  
    	this.prepareStatementFromMedia(stmt, media);
    }

    @Override
	protected PreparedStatement updateStatement(Media media) throws SQLException {
        String mediaSql = "UPDATE Media SET price = ?, title = ?, totalQuantity = ?, weight = ?, description = ?, importDate = ?, rushOrderSupported = ?, imageUrl = ?, productDimension = ?, barcode = ? WHERE media_id = ?";

        try (PreparedStatement mediaStatement = connection.prepareStatement(mediaSql)) {
	        // Thiết lập các tham số cho Media
	        this.prepareStatementFromMedia(mediaStatement, media);
	        mediaStatement.setInt(11, media.getMediaId());
	        
	        return mediaStatement;
        }
    }
    
    // This class is only used for Book, CD and DVD DAOs
    protected MediaAccessDAO() {
    	super();
    }

	@Override
	public String getDaoName() {
		return "media";
	}
}
