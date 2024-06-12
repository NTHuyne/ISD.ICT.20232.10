package com.hust.ict.aims.persistence.dao.media;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hust.ict.aims.entity.media.Dvd;
import com.hust.ict.aims.persistence.dao.media.temp.MediaTemplateDAO;

/**
 * @author
 */
public class DVDDAO extends MediaTemplateDAO<Dvd> {

	@Override
	protected Dvd createItemFromResultSet(ResultSet res) throws SQLException {
    	return new Dvd(
    		mediaAccessDAO.createItemFromResultSet(res),
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
    protected String getAllQuery() throws SQLException {
        return "SELECT * FROM "+
                "DVD INNER JOIN Media " +
                "ON Media.media_id = DVD.media_id ";
    }

    @Override
    protected String getByIdQuery() {
        // Assuming 'connection' is your established JDBC connection
        return "SELECT * FROM DVD INNER JOIN Media ON Media.media_id = DVD.media_id WHERE Media.media_id = ?;";
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
    protected String addQuery() {
    	return "INSERT INTO DVD (dvdType, director, runtime, studio, language, subtitles, releasedDate, genre, media_id) "
    			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }
    @Override 
    protected void addParams(PreparedStatement dvdStatement, Dvd dvd) throws SQLException {
        // Thêm thông tin vào bảng DVD
        this.prepareStatementFromDVD(dvdStatement, dvd);

    }
    
    @Override 
    protected PreparedStatement updateStatement(Dvd dvd) throws SQLException {
        // DVD (dvdType, director, runtime, studio, language, subtitles, releasedDate, genre, media_id)
        String dvdSql = "UPDATE DVD SET dvdType = ?, director = ?, runtime = ?, studio = ?, language = ?, subtitles = ?, releasedDate = ?, genre = ? WHERE media_id = ?";
        PreparedStatement dvdStatement = connection.prepareStatement(dvdSql);
        this.prepareStatementFromDVD(dvdStatement, dvd);

        return dvdStatement;
    }

	@Override
	public String getDaoName() {
		return "DVD";
	}
}
