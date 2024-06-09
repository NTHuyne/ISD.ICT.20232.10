package com.hust.ict.aims.persistence.dao.media;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hust.ict.aims.entity.media.CdAndLp;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.utils.ErrorAlert;
import com.hust.ict.aims.utils.InformationAlert;

/**
 * @author
 */
public class CDDAO extends MediaAccessDAO {

    private CdAndLp createCDFromResultSet(ResultSet res) throws SQLException {
    	return new CdAndLp(
			createMediaFromResultSet(res),
        	res.getString("artists"),
        	res.getString("recordLabel"),
        	res.getString("trackList"),
        	res.getDate("releaseDate"),
        	res.getString("genre"),
        	res.getBoolean("isCD")
        );
    }

    @Override
    public List<Media> getAllMedia() throws SQLException {
        List<Media> medialist = new ArrayList<>();

        String sql = "SELECT * FROM "+
              "CD_and_LP as CD " +
              "INNER JOIN Media " +
              "ON Media.media_id = CD.media_id ";

        // Create statement
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(sql);

        while (res.next()) {
            CdAndLp cdAndlp = this.createCDFromResultSet(res);

            medialist.add(cdAndlp);
        }

        return medialist;
    }

    public CdAndLp getCDAndLPById(int cdAndLpId) {
        // Assuming 'connection' is your established JDBC connection
        String sql = "SELECT * FROM CD_and_LP as CD INNER JOIN Media ON Media.media_id = CD.media_id WHERE media_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cdAndLpId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return this.createCDFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching DVD");
            e.printStackTrace();
        }
        return null;
    }
//    @Override
//    public Media getMediaById(int id) throws SQLException {
//        String sql = "SELECT * FROM "+
//                "cd_and_lp as CD " +
//                "INNER JOIN Media " +
//                "ON Media.id = CD.id " +
//                "where Media.id = " + id + ";";
//
//        Connection conn = null;
//        // Connnect to database
//        conn = ConnectJDBC.getConnection();
//        // Create statement
//        Statement stmt = conn.createStatement();
//        ResultSet res = stmt.executeQuery(sql);
//        CdAndLp cdAndLp = new CdAndLp();
//        if(res.next()) {
//
//            // from media table
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
//            // from CD table
//            String artists = res.getString("artists");
//            String recordLabel = res.getString("recordLabel");
//            String trackList = res.getString("trackList");
//            String musicType = res.getString("musicType");
//            cdAndLp =new CdAndLp(id, category, price, value, title, description, quantity, importDate, barcode, productDimension, imageUrl,
//                    artists, recordLabel, trackList, musicType);
//        } else {
//            throw new SQLException();
//        }
//        return cdAndLp;
//    }
    private void prepareStatementFromCD(PreparedStatement cdAndLpStatement, CdAndLp cdAndLp) throws SQLException {
    	cdAndLpStatement.setBoolean(1, cdAndLp.getIsCD());
        cdAndLpStatement.setString(2, cdAndLp.getArtists());
        cdAndLpStatement.setString(3, cdAndLp.getRecordLabel());
        cdAndLpStatement.setString(4, cdAndLp.getTrackList());
        cdAndLpStatement.setString(5, cdAndLp.getGenre());

        if (cdAndLp.getReleaseDate() != null) {
            cdAndLpStatement.setDate(6, new java.sql.Date(cdAndLp.getReleaseDate().getTime()));
        } else {
            cdAndLpStatement.setNull(6, java.sql.Types.DATE);
        }

        cdAndLpStatement.setInt(7, cdAndLp.getMediaId());
    }

    @Override
    public void addMedia(Media media) throws SQLException {
        if (this.isTitleTaken(media.getTitle())) {
            ErrorAlert errorAlert = new ErrorAlert();
            errorAlert.createAlert("Error Message", null, "Media title is already taken");
            errorAlert.show();
            throw new IllegalArgumentException(media.getTitle() + " is already taken");
        }

        // Bắt đầu giao dịch
        connection.setAutoCommit(false);

        try {
            CdAndLp cdAndLp = (CdAndLp) media;

            // Insert media table
            cdAndLp.setMediaId(this.addTempMedia(media));


            // Thêm thông tin vào bảng CD
            String cdSql = "INSERT INTO CD_and_LP (isCD, artists, recordLabel, trackList, genre, releaseDate, media_id) "
            		+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement cdStatement = connection.prepareStatement(cdSql)) {
            	this.prepareStatementFromCD(cdStatement, cdAndLp);

            	cdStatement.executeUpdate();
            }

            System.out.println("Successfully added LP/CD: " + cdAndLp);

            InformationAlert alert = new InformationAlert();
            alert.createAlert("Information Message", null, "Successfully added LP/CD" );
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
            CdAndLp cdAndLp = (CdAndLp) media;

            // Update Media table
            this.updateTempMedia(media);

            // Update CD_and_LP table
            String cdAndLpSql = "UPDATE CD_and_LP SET isCD = ?, artists = ?, recordLabel = ?, trackList = ?, genre = ?, releaseDate = ? WHERE media_id = ?";
            try (PreparedStatement cdAndLpStatement = connection.prepareStatement(cdAndLpSql)) {
            	this.prepareStatementFromCD(cdAndLpStatement, cdAndLp);

                cdAndLpStatement.executeUpdate();
            }

            System.out.println("Successfully updated CD/ LP: " + media);
            InformationAlert alert = new InformationAlert();
            alert.createAlert("Information Message", null, "Successfully updated CD/ LP" );
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
