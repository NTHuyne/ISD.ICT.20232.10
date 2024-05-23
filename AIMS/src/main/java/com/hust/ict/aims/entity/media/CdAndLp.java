package com.hust.ict.aims.entity.media;

import java.util.Date;

public class CdAndLp extends Media{
    private String artists;
    private String recordLabel;
    private String trackList;
    private Date releaseDate;
    private String musicType;

    public CdAndLp(){
        super();
    }

    public CdAndLp(Integer id, String category, int price, int value, String title, String description, int quantity, Date importDate, Boolean rushOrderSupport, String barcode, String productDimension, String imageUrl, String artists, String recordLabel, String trackList, Date releaseDate, String musicType) {
        super(id, category, price, value, title, description, quantity, importDate, rushOrderSupport, barcode, productDimension, imageUrl);
        this.setArtists(artists);
        this.setRecordLabel(recordLabel);
        this.setTrackList(trackList);
        this.setReleaseDate(releaseDate);
        this.setMusicType(musicType);
    }

    public CdAndLp(Integer id, String category, int price, int value, String title, String description, int quantity, Date importDate, String barcode, String productDimension, String imageUrl, String artists, String recordLabel, String trackList, String musicType) {
        super(id, category, price, value, title, description, quantity, importDate, barcode, productDimension, imageUrl);
        this.setArtists(artists);
        this.setRecordLabel(recordLabel);
        this.setTrackList(trackList);
        this.setMusicType(musicType);
    }

    public CdAndLp(Media media, String trackList, String musicType, String artists, String recordLabel, Date releaseDate) {
        super(media.getId(), media.getCategory(), media.getPrice(),
                media.getValue(), media.getTitle(),  media.getDescription(),
                media.getQuantity(), media.getImportDate(),  media.getRushOrderSupport(),
                media.getProductDimension(), media.getBarcode(), media.getImageUrl());
        this.setArtists(artists);
        this.setRecordLabel(recordLabel);
        this.setTrackList(trackList);
        this.setReleaseDate(releaseDate);
        this.setMusicType(musicType);
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getRecordLabel() {
        return recordLabel;
    }

    public void setRecordLabel(String recordLabel) {
        this.recordLabel = recordLabel;
    }

    public String getTrackList() {
        return trackList;
    }

    public void setTrackList(String trackList) {
        this.trackList = trackList;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMusicType() {
        return musicType;
    }

    public void setMusicType(String musicType) {
        this.musicType = musicType;
    }
}
