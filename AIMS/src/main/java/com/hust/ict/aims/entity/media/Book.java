package com.hust.ict.aims.entity.media;

import java.util.Date;

public class Book extends Media{
    private String authors;
    private String hardCover;
    private String publisher;
    private Date publicationDate;
    private int pages;
    private String language;
    private String bookCategory;

    public Book(Integer id, String category, int price, int value, String title, String description, int quantity, Date importDate, Boolean rushOrderSupport, String barcode, String productDimension, String imageUrl, String authors, String hardCover, String publisher, Date publicationDate, int pages, String language, String bookCategory) {
        super(id, category, price, value, title, description, quantity, importDate, rushOrderSupport, barcode, productDimension, imageUrl);
        this.setAuthors(authors);
        this.setHardCover(hardCover);
        this.setPublisher(publisher);
        this.setPublicationDate(publicationDate);
        this.setPages(pages);
        this.setLanguage(language);
        this.setBookCategory(bookCategory);
    }

    public Book(Integer id, String category, int price, int value, String title, String description, int quantity, Date importDate, String barcode, String productDimension, String imageUrl, String authors, String hardCover, String publisher, Date publicationDate, int pages, String language, String bookCategory) {
        super(id, category, price, value, title, description, quantity, importDate, barcode, productDimension, imageUrl);
        this.setAuthors(authors);
        this.setHardCover(hardCover);
        this.setPublisher(publisher);
        this.setPublicationDate(publicationDate);
        this.setPages(pages);
        this.setLanguage(language);
        this.setBookCategory(bookCategory);
    }

    public Book(Media media, String authors, String hardCover, String publisher, String language, String category, int pages, Date publicationDate) {
        super(media.getId(), media.getCategory(), media.getPrice(), media.getValue(), media.getTitle(),  media.getDescription(), media.getQuantity(), media.getImportDate(),  media.getRushOrderSupport(), media.getProductDimension(), media.getBarcode(), media.getImageUrl());
        this.setAuthors(authors);
        this.setHardCover(hardCover);
        this.setPublisher(publisher);
        this.setLanguage(language);
        this.setBookCategory(category);
        this.setPages(pages);
        this.setPublicationDate(publicationDate);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getHardCover() {
        return hardCover;
    }

    public void setHardCover(String hardCover) {
        this.hardCover = hardCover;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

}
