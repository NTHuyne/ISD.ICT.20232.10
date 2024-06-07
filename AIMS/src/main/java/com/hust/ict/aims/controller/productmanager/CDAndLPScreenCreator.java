package com.hust.ict.aims.controller.productmanager;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.service.productmanager.CDAndLPService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class CDAndLPScreenCreator implements MediaScreenCreator {
    private CDAndLPService cdAndLPService;

    public CDAndLPScreenCreator() {
        this.cdAndLPService = new CDAndLPService(); // Giả sử BookService là một service để xử lý logic liên quan đến sách.
    }

    @Override
    public MediaScreen getMediaScreen(Media media, DataChangedListener data) {
        return new CDAndLPScreen(media, data, cdAndLPService); // Giả sử BookScreen là một class cụ thể để hiển thị thông tin sách.
    }

    @Override
    public Collection<String> getSupportedMediaType() {
        return Arrays.asList("CD", "LP");
    }
}
