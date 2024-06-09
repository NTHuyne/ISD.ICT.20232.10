package com.hust.ict.aims.controller.productmanager;


import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.persistence.dao.media.DVDDAO;

import java.util.Collection;
import java.util.Collections;

public class DVDScreenCreator implements MediaScreenCreator {
    private DVDDAO dvdDAO;

    public DVDScreenCreator() {
        this.dvdDAO = new DVDDAO();
    }

    @Override
    public MediaScreen getMediaScreen(Media media, DataChangedListener data) {
        return new DVDScreen(media, data, dvdDAO);
    }

    @Override
    public Collection<String> getSupportedMediaType() {
        return Collections.singletonList("DVD");
    }
}
