package com.hust.ict.aims.service.productmanager;

import com.hust.ict.aims.persistence.database.ConnectJDBC;
import com.hust.ict.aims.entity.media.Dvd;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.utils.ConfirmationAlert;
import com.hust.ict.aims.utils.ErrorAlert;
import com.hust.ict.aims.utils.InformationAlert;

import java.sql.*;
import java.util.Date;

public class DVDService implements IMediaService{

    private final Connection connection;

    public DVDService() {
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
            // Xử lý thêm Book, bao gồm thông tin của Media
            Dvd dvd = (Dvd) media;

            ConfirmationAlert confirmationAlert = new ConfirmationAlert();
            confirmationAlert.createAlert("Confirmation", null, "Are you sure you want to add this media?");
            confirmationAlert.show();

            if (!confirmationAlert.isConfirmed()) {
                throw new SQLException("Cancel adding media");
            }

            String mediaSql = "INSERT INTO Media (category, price, value, title, description, quantity, importDate, rushOrderSupported, imageUrl, productDimension, barcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement mediaStatement = connection.prepareStatement(mediaSql, Statement.RETURN_GENERATED_KEYS)) {
                // Thiết lập các tham số cho Media
                mediaStatement.setString(1, dvd.getCategory());
                mediaStatement.setInt(2, dvd.getPrice());
                mediaStatement.setInt(3, dvd.getValue());
                mediaStatement.setString(4, dvd.getTitle());
                mediaStatement.setString(5, dvd.getDescription());
                mediaStatement.setInt(6, dvd.getQuantity());

                // Đặt ngày hiện tại cho importDate
                java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
                mediaStatement.setDate(7, sqlDate);

                boolean rushOrderSupported = (dvd.getRushOrderSupport() != null) ? dvd.getRushOrderSupport() : false;
                mediaStatement.setBoolean(8, rushOrderSupported);
                mediaStatement.setString(9, dvd.getImageUrl());
                mediaStatement.setString(10, dvd.getProductDimension());
                mediaStatement.setString(11, dvd.getBarcode());

                int affectedRows = mediaStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating media failed, no rows affected.");
                }

                // Lấy ID được tạo tự động
                try (ResultSet generatedKeys = mediaStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        dvd.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating book failed, no ID obtained.");
                    }
                }

                // Thêm thông tin vào bảng DVD
                String dvdSql = "INSERT INTO DVD (id, dvdType, director, runtime, studio, language, subtitles, releasedDate, filmType) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement dvdStatement = connection.prepareStatement(dvdSql)) {
                    dvdStatement.setInt(1, dvd.getId());
                    dvdStatement.setString(2, dvd.getDvdType());
                    dvdStatement.setString(3, dvd.getDirector());
                    dvdStatement.setInt(4, dvd.getRuntime());
                    dvdStatement.setString(5, dvd.getStudio());
                    dvdStatement.setString(6, dvd.getLanguage());
                    dvdStatement.setString(7, dvd.getSubtitles());
                    dvdStatement.setDate(8, new java.sql.Date(dvd.getReleasedDate().getTime()));
                    dvdStatement.setString(9, dvd.getFilmType());

                    dvdStatement.executeUpdate();
                    System.out.println("Successfully added DVD: " + media);

                    InformationAlert alert = new InformationAlert();
                    alert.createAlert("Information Message", null, "Successfully added DVD" );
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

            Dvd dvd = (Dvd) media;

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

            String dvdSql = "UPDATE DVD SET dvdType = ?, director = ?, runtime = ?, studio = ?, language = ?, subtitles = ?, releasedDate = ?, filmType = ? WHERE id = ?";
            try (PreparedStatement bookStatement = connection.prepareStatement(dvdSql)) {
                bookStatement.setString(1, dvd.getDvdType());
                bookStatement.setString(2, dvd.getDirector());
                bookStatement.setInt(3, dvd.getRuntime());
                bookStatement.setString(4, dvd.getStudio());
                bookStatement.setString(5, dvd.getLanguage());
                bookStatement.setString(6, dvd.getSubtitles());

                if (dvd.getReleasedDate() != null) {
                    bookStatement.setDate(7, new java.sql.Date(dvd.getReleasedDate().getTime()));
                } else {
                    bookStatement.setNull(7, java.sql.Types.DATE);
                }

                bookStatement.setString(8, dvd.getFilmType());
                bookStatement.setInt(9, dvd.getId());

                bookStatement.executeUpdate();
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

    public Dvd fetchDVDFromDatabase(int dvdId) {
        // Assuming 'connection' is your established JDBC connection
        String sql = "SELECT * FROM DVD WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dvdId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Dvd dvd = new Dvd();

                    dvd.setDvdType(resultSet.getString("dvdType"));
                    dvd.setDirector(resultSet.getString("director"));
                    dvd.setRuntime(resultSet.getInt("runtime"));
                    dvd.setStudio(resultSet.getString("studio"));
                    dvd.setLanguage(resultSet.getString("language"));
                    dvd.setSubtitles(resultSet.getString("subtitles"));
                    Date releasedDate = resultSet.getDate("releasedDate");
                    if (releasedDate != null) {
                        dvd.setReleasedDate(releasedDate);
                    }
                    dvd.setFilmType(resultSet.getString("filmType"));

                    return dvd;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching DVD");
            e.printStackTrace();
        }
        return null;
    }
}
