package com.hust.ict.aims.entity.media;

import java.util.Date;

public class Book extends Media {
    private String authors;
    private String coverType;
    private String publisher;
    private Date publicationDate;
    private int pages;
    private String language;
    private String genre;

	public Book(Media otherMedia, String authors, String coverType, String publisher, Date publicationDate, int pages,
			String language, String genre) {
		super(otherMedia);
		this.authors = authors;
		this.coverType = coverType;
		this.publisher = publisher;
		this.publicationDate = publicationDate;
		this.pages = pages;
		this.language = language;
		this.genre = genre;
	}

	public String getAuthors() {
		return authors;
	}
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	public String getCoverType() {
		return coverType;
	}
	public void setCoverType(String coverType) {
		this.coverType = coverType;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public Date getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}

	@Override
	public String getMediaTypeName() {
		return "Book";
	}

}
