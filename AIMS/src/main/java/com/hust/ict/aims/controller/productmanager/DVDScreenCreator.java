package com.hust.ict.aims.controller.productmanager;


import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.service.productmanager.DVDService;

import java.util.Collection;
import java.util.Collections;

public class DVDScreenCreator implements MediaScreenCreator {
    private DVDService dvdService;

    public DVDScreenCreator() {
        this.dvdService = new DVDService();
    }

    @Override
    public MediaScreen getMediaScreen(Media media, DataChangedListener data) {
        return new DVDScreen(media, data, dvdService);
    }

    @Override
    public Collection<String> getSupportedMediaType() {
        return Collections.singletonList("DVD");
    }
}
