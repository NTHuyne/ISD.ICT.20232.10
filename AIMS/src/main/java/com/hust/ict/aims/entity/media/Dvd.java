package com.hust.ict.aims.entity.media;

import java.util.Date;

public class Dvd extends Media{
    private String dvdType;
    private String director;
    private int runtime;
    private String studio;
    private String language;
    private String subtitles;
    private Date releasedDate;
    private String filmType;

    public Dvd() {
        super();
    }

    public Dvd(Integer id, String category, int price, int value, String title, String description, int quantity, Date importDate, Boolean rushOrderSupport, String barcode, String productDimension, String imageUrl, String dvdType, String director, int runtime, String studio, String language, String subtitles, Date releasedDate, String filmType) {
        super(id, category, price, value, title, description, quantity, importDate, rushOrderSupport, barcode, productDimension, imageUrl);
        this.setDvdType(dvdType);
        this.setDirector(director);
        this.setRuntime(runtime);
        this.setStudio(studio);
        this.setLanguage(language);
        this.setSubtitles(subtitles);
        this.setReleasedDate(releasedDate);
        this.setFilmType(filmType);
    }

    public Dvd(Integer id, String category, int price, int value, String title, String description, int quantity, Date importDate, String barcode, String productDimension, String imageUrl, String dvdType, String director, int runtime, String studio, String language, String subtitles, Date releasedDate, String filmType) {
        super(id, category, price, value, title, description, quantity, importDate, barcode, productDimension, imageUrl);
        this.setDvdType(dvdType);
        this.setDirector(director);
        this.setRuntime(runtime);
        this.setStudio(studio);
        this.setLanguage(language);
        this.setSubtitles(subtitles);
        this.setReleasedDate(releasedDate);
        this.setFilmType(filmType);
    }

    public Dvd(Media media, String studio, String filmType, String dvdType, String language, String director, String subtitles, int runtime, Date releasedDate) {
        super(media.getId(), media.getCategory(), media.getPrice(),
                media.getValue(), media.getTitle(),  media.getDescription(),
                media.getQuantity(), media.getImportDate(),  media.getRushOrderSupport(),
                media.getProductDimension(), media.getBarcode(), media.getImageUrl());
        this.setDirector(director);
        this.setRuntime(runtime);
        this.setStudio(studio);
        this.setLanguage(language);
        this.setSubtitles(subtitles);
        this.setFilmType(filmType);
        this.setDvdType(dvdType);
        this.setReleasedDate(releasedDate);
    }

    public String getDvdType() {
        return dvdType;
    }

    public void setDvdType(String dvdType) {
        this.dvdType = dvdType;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(String subtitles) {
        this.subtitles = subtitles;
    }

    public String getFilmType() {
        return filmType;
    }

    public void setFilmType(String filmType) {
        this.filmType = filmType;
    }

    public Date getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(Date releasedDate) {
        this.releasedDate = releasedDate;
    }

}
