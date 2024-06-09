package com.hust.ict.aims.controller.productmanager;

import java.util.Arrays;
import java.util.Collection;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.persistence.dao.media.CDDAO;

public class CDAndLPScreenCreator implements MediaScreenCreator {
    private CDDAO cdAndLPDAO;

    public CDAndLPScreenCreator() {
        this.cdAndLPDAO = new CDDAO(); // Giả sử BookDAO là một service để xử lý logic liên quan đến sách.
    }

    @Override
    public MediaScreen getMediaScreen(Media media, DataChangedListener data) {
        return new CDAndLPScreen(media, data, cdAndLPDAO); // Giả sử BookScreen là một class cụ thể để hiển thị thông tin sách.
    }

    @Override
    public Collection<String> getSupportedMediaType() {
        return Arrays.asList("CD", "LP");
    }
}
