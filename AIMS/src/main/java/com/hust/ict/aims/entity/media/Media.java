package com.hust.ict.aims.entity.media;

import java.util.Date;

public class Media {
    private int mediaId;
    private String title;
    private int price;
    private int totalQuantity;
    private Double weight;
    private Boolean rushOrderSupport;
    private String imageUrl;
    
    private String barcode;
    private String description;
    private Date importDate;
    private String productDimension;

    public Media() {
		super();
	}

	public Media (Media otherMedia){
		this.mediaId = otherMedia.getMediaId();
		this.title = otherMedia.getTitle();
		this.price = otherMedia.getPrice();
		this.totalQuantity = otherMedia.getTotalQuantity();
		this.weight = otherMedia.getWeight();
		this.rushOrderSupport = otherMedia.getRushOrderSupport();
		this.imageUrl = otherMedia.getImageUrl();
		this.barcode = otherMedia.getBarcode();
		this.description = otherMedia.getDescription();
		this.importDate = otherMedia.getImportDate();
		this.productDimension = otherMedia.getProductDimension();
    }

	public Media(int mediaId, String title, int price, int totalQuantity, Double weight, Boolean rushOrderSupport,
			String imageUrl, String barcode, String description, Date importDate, String productDimension) {
		this.mediaId = mediaId;
		this.title = title;
		this.price = price;
		this.totalQuantity = totalQuantity;
		this.weight = weight;
		this.rushOrderSupport = rushOrderSupport;
		this.imageUrl = imageUrl;
		this.barcode = barcode;
		this.description = description;
		this.importDate = importDate;
		this.productDimension = productDimension;
	}

	public String getMediaTypeName() {
		return "Unknown";
	};

	public int getMediaId() {
		return mediaId;
	}

    public void setMediaId(int mediaId) {
		this.mediaId = mediaId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Boolean getRushOrderSupport() {
		return rushOrderSupport;
	}

	public void setRushOrderSupport(Boolean rushOrderSupport) {
		this.rushOrderSupport = rushOrderSupport;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public String getProductDimension() {
		return productDimension;
	}

	public void setProductDimension(String productDimension) {
		this.productDimension = productDimension;
	}
 
}
