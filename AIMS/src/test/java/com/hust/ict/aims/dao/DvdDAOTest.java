package com.hust.ict.aims.dao;

import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import com.hust.ict.aims.entity.media.Book;
import com.hust.ict.aims.entity.media.Dvd;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.persistence.dao.media.BookDAO;
import com.hust.ict.aims.persistence.dao.media.DVDDAO;
import com.hust.ict.aims.persistence.dao.media.MediaDAO;

@TestMethodOrder(OrderAnnotation.class)
public class DvdDAOTest extends AbstractDAOTest<Dvd, DVDDAO>{
	@Override
	public int getExistingItemId() {
		return 3;
	}
	@Override
	public Dvd getExistingItem() {
		Media trueMedia = new Media(
				this.getExistingItemId(),
	        	"Sample DVD",
	        	250000,
	        	75,
	        	0.25,
	        	true,
	        	"sample_dvd.jpg",
	        	"98465",
	        	"This is a sample DVD",
	        	java.sql.Date.valueOf(LocalDate.parse("2022-01-01")),
	        	"17x6x9"
			);
		
			return new Dvd(
				trueMedia,
	        	"Movie",
	        	"Steven Spielberg",
	        	120,
	        	"Universal Pictures",
	        	"English",
	        	"English",
	        	java.sql.Date.valueOf(LocalDate.parse("1993-06-11")),
	        	"Adventure"
	        );
	}

	@Override
	public DVDDAO getDAO() {
		return new DVDDAO();
	}

	@Override
	public void prepareAddItem(Dvd item) {
		item.setTitle("ADDING");
		item.setRuntime(999);
	}

	@Override
	public void prepareUpdateItem(Dvd item) {
		item.setTitle("UPDATING");
		item.setSubtitles("Spanish");
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
