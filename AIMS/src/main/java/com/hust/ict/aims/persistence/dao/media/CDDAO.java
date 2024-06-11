package com.hust.ict.aims.persistence.dao.media;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hust.ict.aims.entity.media.CdAndLp;
import com.hust.ict.aims.persistence.dao.media.temp.MediaTemplateDAO;

/**
 * @author
 */
public class CDDAO extends MediaTemplateDAO<CdAndLp> {
	
	@Override
    protected CdAndLp createItemFromResultSet(ResultSet res) throws SQLException {
    	return new CdAndLp(
    		mediaAccessDAO.createItemFromResultSet(res),
        	res.getString("artists"),
        	res.getString("recordLabel"),
        	res.getString("trackList"),
        	res.getDate("releaseDate"),
        	res.getString("genre"),
        	res.getBoolean("isCD")
        );
    }

    @Override
    protected PreparedStatement getAllStatement() throws SQLException {
        String sql = "SELECT * FROM "+
                "CD_and_LP as CD " +
                "INNER JOIN Media " +
                "ON Media.media_id = CD.media_id ";

        // Create statement
        PreparedStatement stmt = connection.prepareStatement(sql);
        return stmt;
    }

    @Override
    protected PreparedStatement getByIdStatement(int cdAndLpId) throws SQLException {
        // Assuming 'connection' is your established JDBC connection
        String sql = "SELECT * FROM CD_and_LP as CD INNER JOIN Media ON Media.media_id = CD.media_id WHERE media_id = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, cdAndLpId);

        return statement;
    }

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
    protected PreparedStatement addStatement(CdAndLp cdAndLp) throws SQLException {
        // Thêm thông tin vào bảng CD
        String cdSql = "INSERT INTO CD_and_LP (isCD, artists, recordLabel, trackList, genre, releaseDate, media_id) "
        		+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement cdStatement = connection.prepareStatement(cdSql, Statement.RETURN_GENERATED_KEYS);
        this.prepareStatementFromCD(cdStatement, cdAndLp);

        return cdStatement;
    }
    
    @Override 
    protected PreparedStatement updateStatement(CdAndLp cdAndLp) throws SQLException {
        // Update CD_and_LP table
        String cdAndLpSql = "UPDATE CD_and_LP SET isCD = ?, artists = ?, recordLabel = ?, trackList = ?, genre = ?, releaseDate = ? WHERE media_id = ?";
        PreparedStatement cdAndLpStatement = connection.prepareStatement(cdAndLpSql);
        this.prepareStatementFromCD(cdAndLpStatement, cdAndLp);

        return cdAndLpStatement;
    }

	@Override
	protected String getDaoName() {
		return "CD/LP";
	}
}
