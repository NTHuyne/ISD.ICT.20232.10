package com.hust.ict.aims.service.productmanager;

import com.hust.ict.aims.entity.media.Media;

import java.sql.SQLException;

public interface IMediaService {
    void addMedia(Media media) throws SQLException;
    void updateMedia(Media media) throws SQLException;
}
