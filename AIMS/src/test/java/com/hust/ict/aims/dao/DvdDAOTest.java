package com.hust.ict.aims.dao;

import java.sql.SQLException;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.hust.ict.aims.entity.media.CdAndLp;
import com.hust.ict.aims.entity.media.Dvd;
import com.hust.ict.aims.persistence.dao.media.DVDDAO;
import com.hust.ict.aims.persistence.dao.media.MediaDAO;

@TestMethodOrder(OrderAnnotation.class)
public class DvdDAOTest extends AbstractDAOTest<Dvd, DVDDAO>{
	
	@Override
	public int getExistingItemId() {
		return AllDAOData.getExistingDvd().getMediaId();
	}
	@Override
	public Dvd getExistingItem() {
		return AllDAOData.getExistingDvd();
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
	public void deleteStatement(int id) throws SQLException {
		// Doesn't implement delete because that's mediaDAO responsibility
		new MediaDAO().deleteMedia(id);
	}
}
