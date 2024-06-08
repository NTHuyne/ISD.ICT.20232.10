package com.hust.ict.aims.controller.productmanager;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.service.productmanager.BookService;
import com.hust.ict.aims.service.productmanager.CDAndLPService;
import com.hust.ict.aims.service.productmanager.DVDService;

// Su dung Factory pattern
public class MediaScreenFactory {
    public static MediaScreen getMediaScreen(String category, Media media, DataChangedListener data) {

        if (category == null) {
            return null;
        }

        return switch (category.toUpperCase()) {
            case "BOOK" -> {
                BookService bookService = new BookService();
                yield new BookScreen(media, data, bookService);
            }
            case "CD", "LP" -> {
                CDAndLPService cdAndLpService = new CDAndLPService();
                yield new CDAndLPScreen(media, data, cdAndLpService);
            }
            case "DVD" -> {
                DVDService dvdService = new DVDService();
                yield new DVDScreen(media, data, dvdService);
            }
            default -> null;
        };
    }
}
