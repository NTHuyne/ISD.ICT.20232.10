package com.hust.ict.aims.entity.media;

import java.util.Date;

public class CdAndLp extends Media{
    private String artists;
    private String recordLabel;
    private String trackList;
    private Date releaseDate;
    private String genre;
    private Boolean isCD;

	public CdAndLp(Media otherMedia, String artists, String recordLabel, String trackList, Date releaseDate,
			String genre, Boolean isCD) {
		super(otherMedia);
		this.artists = artists;
		this.recordLabel = recordLabel;
		this.trackList = trackList;
		this.releaseDate = releaseDate;
		this.genre = genre;
		this.isCD = isCD;
	}
	
	public CdAndLp() {
		super();
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

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Boolean getIsCD() {
		return isCD;
	}

	public void setIsCD(Boolean isCD) {
		this.isCD = isCD;
	}

	@Override
	public String getMediaTypeName() {
		if (this.isCD) {
			return "CD";
		} else {
			return "LP";
		}
	}
}
