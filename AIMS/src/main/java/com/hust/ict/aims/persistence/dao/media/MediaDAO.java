package com.hust.ict.aims.persistence.dao.media;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.persistence.database.ConnectJDBC;
import com.hust.ict.aims.utils.InformationAlert;

public class MediaDAO {
	public List<Media> getAllMedia() throws SQLException {
		List<Media> mergedList = new BookDAO().getAllMedia();
		mergedList.addAll(new CDDAO().getAllMedia());
		mergedList.addAll(new DVDDAO().getAllMedia());
		return mergedList;
	}

    public void deleteMedia(int mediaId) throws SQLException {
    	Connection connection = ConnectJDBC.getConnection();

        String sql = "DELETE FROM Media WHERE media_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, mediaId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting media failed, no rows affected.");
            } else {
                System.out.println("Successfully deleted media with ID: " + mediaId);
                InformationAlert alert = new InformationAlert();
                alert.createAlert("Information Message", null, "Successfully deleted media");
                alert.show();
            }
        }
    }

}
