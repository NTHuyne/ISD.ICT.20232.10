package com.hust.ict.aims.service.productmanager;

import com.hust.ict.aims.persistence.database.ConnectJDBC;
import com.hust.ict.aims.entity.media.CdAndLp;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.utils.ConfirmationAlert;
import com.hust.ict.aims.utils.ErrorAlert;
import com.hust.ict.aims.utils.InformationAlert;

import java.sql.*;
import java.util.Date;

public class CDAndLPService implements IMediaService{

    private final Connection connection;

    public CDAndLPService() {
        this.connection = ConnectJDBC.getConnection();
    }

    private boolean isTitleTaken(String title) throws SQLException {
        String checkTitleSql = "SELECT title FROM Media WHERE title = ?";
        try (PreparedStatement statement = connection.prepareStatement(checkTitleSql)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
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
            CdAndLp cdAndLp = (CdAndLp) media;

            ConfirmationAlert confirmationAlert = new ConfirmationAlert();
            confirmationAlert.createAlert("Confirmation", null, "Are you sure you want to add this media?");
            confirmationAlert.show();

            if (!confirmationAlert.isConfirmed()) {
                throw new SQLException("Cancel adding media");
            }

            String mediaSql = "INSERT INTO Media (category, price, value, title, description, quantity, importDate, rushOrderSupported, imageUrl, productDimension, barcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement mediaStatement = connection.prepareStatement(mediaSql, Statement.RETURN_GENERATED_KEYS)) {
                // Thiết lập các tham số cho Media
                mediaStatement.setString(1, cdAndLp.getCategory());
                mediaStatement.setInt(2, cdAndLp.getPrice());
                mediaStatement.setInt(3, cdAndLp.getValue());
                mediaStatement.setString(4, cdAndLp.getTitle());
                mediaStatement.setString(5, cdAndLp.getDescription());
                mediaStatement.setInt(6, cdAndLp.getQuantity());

                // Đặt ngày hiện tại cho importDate
                java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
                mediaStatement.setDate(7, sqlDate);

                boolean rushOrderSupported = (cdAndLp.getRushOrderSupport() != null) ? cdAndLp.getRushOrderSupport() : false;
                mediaStatement.setBoolean(8, rushOrderSupported);
                mediaStatement.setString(9, cdAndLp.getImageUrl());
                mediaStatement.setString(10, cdAndLp.getProductDimension());
                mediaStatement.setString(11, cdAndLp.getBarcode());

                int affectedRows = mediaStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating media failed, no rows affected.");
                }

                // Lấy ID được tạo tự động
                try (ResultSet generatedKeys = mediaStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cdAndLp.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating book failed, no ID obtained.");
                    }
                }

                // Thêm thông tin vào bảng DVD
                String dvdSql = "INSERT INTO CD_and_LP (id, artists, recordLabel, trackList, releaseDate, musicType) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement dvdStatement = connection.prepareStatement(dvdSql)) {
                    dvdStatement.setInt(1, cdAndLp.getId());
                    dvdStatement.setString(2, cdAndLp.getArtists());
                    dvdStatement.setString(3, cdAndLp.getRecordLabel());
                    dvdStatement.setString(4, cdAndLp.getTrackList());
                    dvdStatement.setDate(5, new java.sql.Date(cdAndLp.getReleaseDate().getTime()));
                    dvdStatement.setString(6, cdAndLp.getMusicType());
                    dvdStatement.executeUpdate();
                    System.out.println("Successfully added LP/CD: " + media);

                    InformationAlert alert = new InformationAlert();
                    alert.createAlert("Information Message", null, "Successfully added LP/CD" );
                    alert.show();
                }
            }

            connection.commit(); // Hoàn thành giao dịch
        } catch (SQLException e) {
            connection.rollback(); // Hủy bỏ giao dịch
            throw e;
        } finally {
            connection.setAutoCommit(true); // Khôi phục auto-commit
        }
    }

    public void updateMedia(Media media) throws SQLException {
        // Start transaction
        connection.setAutoCommit(false);

        try {

            CdAndLp cdAndLp = (CdAndLp) media;

            // Update Media table
            String mediaSql = "UPDATE Media SET category = ?, price = ?, value = ?, title = ?, description = ?, quantity = ?, importDate = ?, rushOrderSupported = ?, imageUrl = ?, productDimension = ?, barcode = ? WHERE id = ?";
            try (PreparedStatement mediaStatement = connection.prepareStatement(mediaSql)) {
                mediaStatement.setString(1, media.getCategory());
                mediaStatement.setInt(2, media.getPrice());
                mediaStatement.setInt(3, media.getValue());
                mediaStatement.setString(4, media.getTitle());
                mediaStatement.setString(5, media.getDescription());
                mediaStatement.setInt(6, media.getQuantity());

                java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
                mediaStatement.setDate(7, sqlDate);

                boolean rushOrderSupported = (media.getRushOrderSupport() != null) ? media.getRushOrderSupport() : false;
                mediaStatement.setBoolean(8, rushOrderSupported);
                mediaStatement.setString(9, media.getImageUrl());
                mediaStatement.setString(10, media.getProductDimension());
                mediaStatement.setString(11, media.getBarcode());
                mediaStatement.setInt(12, media.getId());

                ConfirmationAlert confirmationAlert = new ConfirmationAlert();
                confirmationAlert.createAlert("Confirmation", null, "Are you sure you want to update this media?");
                confirmationAlert.show();

                if (!confirmationAlert.isConfirmed()) {
                    throw new SQLException("Cancel updating media");
                }

                int affectedRows = mediaStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Updating media failed, no rows affected.");
                }

            }

            String cdAndLpSql = "UPDATE CD_and_LP SET artists = ?, recordLabel = ?, trackList = ?, releaseDate = ?, musicType = ? WHERE id = ?";
            try (PreparedStatement cdAndLpStatement = connection.prepareStatement(cdAndLpSql)) {
                cdAndLpStatement.setString(1, cdAndLp.getArtists());
                cdAndLpStatement.setString(2, cdAndLp.getRecordLabel());
                cdAndLpStatement.setString(3, cdAndLp.getTrackList());

                if (cdAndLp.getReleaseDate() != null) {
                    cdAndLpStatement.setDate(4, new java.sql.Date(cdAndLp.getReleaseDate().getTime()));
                } else {
                    cdAndLpStatement.setNull(4, java.sql.Types.DATE);
                }

                cdAndLpStatement.setString(5, cdAndLp.getMusicType());
                cdAndLpStatement.setInt(6, cdAndLp.getId());

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

    public CdAndLp fetchCDAndLPFromDatabase(int cdAndLpId) {
        // Assuming 'connection' is your established JDBC connection
        String sql = "SELECT * FROM CD_and_LP WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cdAndLpId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    CdAndLp cdAndLp = new CdAndLp();

                    cdAndLp.setArtists(resultSet.getString("artists"));
                    cdAndLp.setMusicType(resultSet.getString("musicType"));
                    cdAndLp.setRecordLabel(resultSet.getString("recordLabel"));
                    cdAndLp.setTrackList(resultSet.getString("trackList"));
                    Date releasedDate = resultSet.getDate("releaseDate");
                    if (releasedDate != null) {
                        cdAndLp.setReleaseDate(releasedDate);
                    }
                    return cdAndLp;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching DVD");
            e.printStackTrace();
        }
        return null;
    }
}
