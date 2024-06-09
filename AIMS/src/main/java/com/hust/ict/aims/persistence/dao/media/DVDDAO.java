package com.hust.ict.aims.persistence.dao.media;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hust.ict.aims.entity.media.Dvd;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.persistence.database.ConnectJDBC;
import com.hust.ict.aims.utils.ErrorAlert;
import com.hust.ict.aims.utils.InformationAlert;

/**
 * @author
 */
public class DVDDAO extends MediaAccessDAO {
    private Dvd createDVDFromResultSet(ResultSet res) throws SQLException {
    	return new Dvd(
			createMediaFromResultSet(res),
        	res.getString("dvdType"),
        	res.getString("director"),
        	res.getInt("runtime"),
        	res.getString("studio"),
        	res.getString("language"),
        	res.getString("subtitles"),
        	res.getDate("releasedDate"),
        	res.getString("genre")
        );
    }

	@Override
    public List<Media> getAllMedia() throws SQLException {
        List<Media> medialist = new ArrayList<>();

        String sql = "SELECT * FROM "+
              "DVD INNER JOIN Media " +
              "ON Media.media_id = DVD.media_id ";

        Connection conn = null;
        // Connnect to database
        conn = ConnectJDBC.getConnection();
        // Create statement
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(sql);

        while (res.next()) {
            Dvd dvd = this.createDVDFromResultSet(res);

            medialist.add(dvd);
        }

        return medialist;
    }
//    @Override
//    public Media getMediaById(int id) throws SQLException {
//        String sql = "SELECT * FROM "+
//                "DVD " +
//                "INNER JOIN Media " +
//                "ON Media.id = DVD.id " +
//                "where Media.id = " + id + ";";
//        Connection conn = null;
//        // Connnect to database
//        conn = ConnectJDBC.getConnection();
//        // Create statement
//        Statement stmt = conn.createStatement();
//        ResultSet res = stmt.executeQuery(sql);
//        Dvd dvd = new Dvd();
//        if(res.next()) {
//
//// from media table
//            String category = res.getString("category");
//            int price = res.getInt("price");
//            int value = res.getInt("value");
//            String title = res.getString("title");
//            String description = res.getString("description");
//            int quantity = res.getInt("quantity");
//            String barcode = res.getString("barcode");
//            Date importDate = res.getDate("importDate");
//            String imageUrl = res.getString("imageUrl");
//            String productDimension = res.getString("productDimension");
//
//            // from DVD table
//            String dvdType = res.getString("dvdType");
//            String director = res.getString("director");
//            int runtime = res.getInt("runtime");
//            String studio = res.getString("studio");
//            String language = res.getString("language");
//            String subtitles = res.getString("subtitles");
//            Date releasedDate = res.getDate("releasedDate");
//            String filmType = res.getString("filmType");
//
//            dvd = new Dvd(id, category, price, value, title, description, quantity, importDate, barcode, productDimension, imageUrl,
//                    dvdType, director, runtime, studio, language, subtitles, releasedDate, filmType);
//        } else {
//            throw new SQLException();
//        }
//        return dvd;
//    }
    public Dvd getDvdById(int dvdId) {
        // Assuming 'connection' is your established JDBC connection
        String sql = "SELECT * FROM DVD INNER JOIN Media ON Media.media_id = DVD.media_id WHERE media_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dvdId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return this.createDVDFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching DVD");
            e.printStackTrace();
        }
        return null;
    }



    // DVD (dvdType, director, runtime, studio, language, subtitles, releasedDate, genre, media_id)
    private void prepareStatementFromDVD(PreparedStatement dvdStatement, Dvd dvd) throws SQLException {
        dvdStatement.setString(1, dvd.getDvdType());
        dvdStatement.setString(2, dvd.getDirector());
        dvdStatement.setInt(3, dvd.getRuntime());
        dvdStatement.setString(4, dvd.getStudio());
        dvdStatement.setString(5, dvd.getLanguage());
        dvdStatement.setString(6, dvd.getSubtitles());

        if (dvd.getReleasedDate() != null) {
        	dvdStatement.setDate(7, new java.sql.Date(dvd.getReleasedDate().getTime()));
        } else {
        	dvdStatement.setNull(7, java.sql.Types.DATE);
        }

        dvdStatement.setString(8, dvd.getGenre());

        dvdStatement.setInt(9, dvd.getMediaId());
    }


    @Override
    public void addMedia(Media media) throws SQLException {
        if (isTitleTaken(media.getTitle())) {
            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Error Message", null, "Media title is already taken");
            errorAlert.show();
            throw new IllegalArgumentException(media.getTitle() + " is already taken");
        }

        // Bắt đầu giao dịch
        connection.setAutoCommit(false);

        try {
            // Xử lý thêm Book, bao gồm thông tin của Media
            Dvd dvd = (Dvd) media;

            dvd.setMediaId(this.addTempMedia(media));

            // Thêm thông tin vào bảng DVD
            String dvdSql = "INSERT INTO DVD (dvdType, director, runtime, studio, language, subtitles, releasedDate, genre, media_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement dvdStatement = connection.prepareStatement(dvdSql)) {
            	this.prepareStatementFromDVD(dvdStatement, dvd);

                dvdStatement.executeUpdate();
            }

            System.out.println("Successfully added DVD: " + media);

            InformationAlert alert = new InformationAlert();
            alert.createAlert("Information Message", null, "Successfully added DVD" );
            alert.show();


            connection.commit(); // Hoàn thành giao dịch
        } catch (SQLException e) {
            connection.rollback(); // Hủy bỏ giao dịch
            throw e;
        } finally {
            connection.setAutoCommit(true); // Khôi phục auto-commit
        }
    }

    @Override
    public void updateMedia(Media media) throws SQLException {
        // Start transaction
        connection.setAutoCommit(false);

        try {
            Dvd dvd = (Dvd) media;

            // Update Media table
            this.updateTempMedia(media);

         // DVD (dvdType, director, runtime, studio, language, subtitles, releasedDate, genre, media_id)
            String dvdSql = "UPDATE DVD SET dvdType = ?, director = ?, runtime = ?, studio = ?, language = ?, subtitles = ?, releasedDate = ?, genre = ? WHERE media_id = ?";
            try (PreparedStatement dvdStatement = connection.prepareStatement(dvdSql)) {
                this.prepareStatementFromDVD(dvdStatement, dvd);

                dvdStatement.executeUpdate();
            }

            System.out.println("Successfully updated DVD: " + media);
            InformationAlert alert = new InformationAlert();
            alert.createAlert("Information Message", null, "Successfully updated DVD" );
            alert.show();

            connection.commit(); // Commit transaction
        } catch (SQLException e) {
            connection.rollback(); // Roll back transaction
            throw e;
        } finally {
            connection.setAutoCommit(true); // Restore auto-commit
//            System.out.println("Updating media with ID: " + media.getId());
        }
    }
}
