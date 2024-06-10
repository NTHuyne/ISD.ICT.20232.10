package com.hust.ict.aims.controller.productmanager;

import java.util.Collection;

import com.hust.ict.aims.entity.media.Media;

public interface MediaScreenCreator {
    MediaScreen getMediaScreen(Media media, DataChangedListener data);
    Collection<String> getSupportedMediaType();
}
