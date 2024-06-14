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
    private String genre;

    public Dvd(Media otherMedia, String dvdType, String director, int runtime, String studio, String language,
			String subtitles, Date releasedDate, String genre) {
		super(otherMedia);
		this.dvdType = dvdType;
		this.director = director;
		this.runtime = runtime;
		this.studio = studio;
		this.language = language;
		this.subtitles = subtitles;
		this.releasedDate = releasedDate;
		this.genre = genre;
	}
    
	public Dvd() {
		super();
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Date getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(Date releasedDate) {
        this.releasedDate = releasedDate;
    }

	@Override
	public String getMediaTypeName() {
		return "DVD";
	}
}
