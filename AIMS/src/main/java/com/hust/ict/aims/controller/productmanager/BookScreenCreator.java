package com.hust.ict.aims.controller.productmanager;

import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.service.productmanager.BookService;

import java.util.Collection;
import java.util.Collections;

public class BookScreenCreator implements MediaScreenCreator {
    private BookService bookService;

    public BookScreenCreator() {
        this.bookService = new BookService(); // Giả sử BookService là một service để xử lý logic liên quan đến sách.
    }

    @Override
    public MediaScreen getMediaScreen(Media media, DataChangedListener data) {
        return new BookScreen(media, data, bookService); // Giả sử BookScreen là một class cụ thể để hiển thị thông tin sách.
    }

    @Override
    public Collection<String> getSupportedMediaType() {
        return Collections.singletonList("BOOK");
    }
}
