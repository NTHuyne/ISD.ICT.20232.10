package com.hust.ict.aims.dao;

import java.sql.SQLException;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import com.hust.ict.aims.entity.media.CdAndLp;
import com.hust.ict.aims.persistence.dao.media.CDDAO;
import com.hust.ict.aims.persistence.dao.media.MediaDAO;

@TestMethodOrder(OrderAnnotation.class)
public class CdDAOTest extends AbstractDAOTest<CdAndLp, CDDAO>{
	
	@Override
	public int getExistingItemId() {
		return AllDAOData.getExistingCd().getMediaId();
	}
	
	@Override
	public CdAndLp getExistingItem() {
		return AllDAOData.getExistingCd();
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
