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

    public boolean isTitleTaken(String title) throws SQLException {
        String checkTitleSql = "SELECT title FROM Media WHERE title = ?";
        try (PreparedStatement statement = connection.prepareStatement(checkTitleSql)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
    }

    @Override
    protected PreparedStatement addStatement(Media media) throws SQLException {
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
	        return mediaStatement;
        }
    }

    @Override
	protected PreparedStatement updateStatement(Media media) throws SQLException {
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
	        
	        return mediaStatement;
        }
    }
    
    // This class is only used for Book, CD and DVD DAOs
    protected MediaAccessDAO() {
    	super();
    }

	@Override
	protected String getDaoName() {
		return "media";
	}
}
