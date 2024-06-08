package com.hust.ict.aims.controller.productmanager;

import com.hust.ict.aims.entity.media.Media;

import java.util.Collection;

public interface MediaScreenCreator {
    MediaScreen getMediaScreen(Media media, DataChangedListener data);
    Collection<String> getSupportedMediaType();
}
