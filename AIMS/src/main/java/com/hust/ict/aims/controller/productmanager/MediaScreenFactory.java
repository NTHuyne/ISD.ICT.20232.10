package com.hust.ict.aims.controller.productmanager;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.persistence.dao.media.BookDAO;
import com.hust.ict.aims.persistence.dao.media.CDDAO;
import com.hust.ict.aims.persistence.dao.media.DVDDAO;


// Su dung Factory pattern
public class MediaScreenFactory {
    public static MediaScreen getMediaScreen(String category, Media media, DataChangedListener data) {

        if (category == null) {
            return null;
        }

        return switch (category.toUpperCase()) {
            case "BOOK" -> {
                BookDAO bookDAO = new BookDAO();
                yield new BookScreen(media, data, bookDAO);
            }
            case "CD", "LP" -> {
                CDDAO cdAndLpDAO = new CDDAO();
                yield new CDAndLPScreen(media, data, cdAndLpDAO);
            }
            case "DVD" -> {
                DVDDAO dvdDAO = new DVDDAO();
                yield new DVDScreen(media, data, dvdDAO);
            }
            default -> null;
        };
    }
}
