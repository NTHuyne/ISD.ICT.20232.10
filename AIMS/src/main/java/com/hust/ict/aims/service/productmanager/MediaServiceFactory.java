package com.hust.ict.aims.service.productmanager;

import com.hust.ict.aims.entity.media.Book;
import com.hust.ict.aims.entity.media.CdAndLp;
import com.hust.ict.aims.entity.media.Dvd;
import com.hust.ict.aims.entity.media.Media;

public class MediaServiceFactory {
    public static IMediaService getHandler(Media media) {
        if (media instanceof Book) {
            return new BookService();
        } else if (media instanceof Dvd) {
            return new DVDService();
        } else if (media instanceof CdAndLp) {
            return new CDAndLPService();
        } else {
            System.out.println("Media is not instanceof either Book, CD, DVD or LP");
        }
        return null;
    }
}
