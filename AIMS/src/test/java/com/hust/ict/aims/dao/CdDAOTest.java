package com.hust.ict.aims.dao;

import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import com.hust.ict.aims.entity.media.CdAndLp;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.persistence.dao.media.CDDAO;
import com.hust.ict.aims.persistence.dao.media.MediaDAO;

@TestMethodOrder(OrderAnnotation.class)
public class CdDAOTest extends AbstractDAOTest<CdAndLp, CDDAO>{
	@Override
	public int getExistingItemId() {
		return 2;
	}
	
	@Override
	public CdAndLp getExistingItem() {
		Media trueMedia = new Media(
	        	this.getExistingItemId(),
	        	"Sample CD",
	        	150000,
	        	50,
	        	0.25,
	        	false,
	        	"sample_cd.jpg",
	        	"56789",
	        	"This is a sample CD",
	        	java.sql.Date.valueOf(LocalDate.parse("2022-01-01")),
	        	"15x6x9"
			);
		
			return new CdAndLp(
				trueMedia,
	        	"Artist Name",
	        	"Record Label",
	        	"Track 1, Track 2, Track 3",
	        	java.sql.Date.valueOf(LocalDate.parse("2022-01-01")),
	        	"Pop",
	        	true
	        );
	}

	@Override
	public CDDAO getDAO() {
		return new CDDAO();
	}

	@Override
	public void prepareAddItem(CdAndLp item) {
		item.setTitle("ADDING");
		item.setRecordLabel("Source Music");
	}

	@Override
	public void prepareUpdateItem(CdAndLp item) {
		item.setTitle("UPDATING");
		item.setRecordLabel("Universal Music Group");
	}
	
	@Override
	public String[] excludeFieldsForAdd() {
		String[] exclude = {"importDate"};
		return exclude;
	}
	
	@Override
	public void deleteStatement(int id) throws SQLException {
		// Doesn't implement delete because that's mediaDAO responsibility
		new MediaDAO().deleteMedia(id);
	}

}
