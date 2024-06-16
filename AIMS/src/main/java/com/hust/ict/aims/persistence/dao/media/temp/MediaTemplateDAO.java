package com.hust.ict.aims.persistence.dao.media.temp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.persistence.dao.TemplateDAO;
import com.hust.ict.aims.utils.ErrorAlert;
import com.hust.ict.aims.utils.InformationAlert;

public abstract class MediaTemplateDAO<T extends Media> extends TemplateDAO<T> {
	protected MediaAccessDAO mediaAccessDAO = new MediaAccessDAO(connection);	// Reusing connection

	public List<Media> getAllMedia() throws SQLException {
        List<Media> itemlist = new ArrayList<>();

        PreparedStatement stmt = connection.prepareStatement(this.getAllQuery());
        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            Media item = this.createItemFromResultSet(res);
            itemlist.add(item);
        }

        return itemlist;
    }
	
	@Override
    public int add(T media) throws SQLException {
        // Bắt đầu giao dịch
        connection.setAutoCommit(false);

        try {
        	media.setMediaId(mediaAccessDAO.add(media));
        	super.setNoReturnGeneratedKeys(true);
        	super.add(media);
            connection.commit(); // Hoàn thành giao dịch
            
            System.out.println("Successfully added " + this.getDaoName() + ": " + media);
            return media.getMediaId();
        } catch (SQLException e) {
            connection.rollback(); // Hủy bỏ giao dịch
            throw e;
        } finally {
            connection.setAutoCommit(true); // Khôi phục auto-commit
        }
    }

    @Override
    public void update(T media) throws SQLException {
        // Start transaction
        connection.setAutoCommit(false);

        try {
            // Update Media table
            mediaAccessDAO.update(media);
            super.update(media);
            
            connection.commit(); // Commit transaction    
        } catch (SQLException e) {
            connection.rollback(); // Roll back transaction
            throw e;
        } finally {
            connection.setAutoCommit(true); // Restore auto-commit
        }
    }
}
