package com.hust.ict.aims.entity.media;

import java.util.Date;

public class Media {
    private int id;
    private String category;
    private int price;
    private int value;
    private String title;
    private String description;
    private String barcode;
    private int quantity;
    private Date importDate;
    private Boolean rushOrderSupport;
    private String imageUrl;
    private String productDimension;

    public Media (){
    }

    public Media(int id, String title, int price, String category, int quantity) {
        this.setId(id);
        this.setCategory(category);
        this.setPrice(price);
        this.setTitle(title);
        this.setQuantity(quantity);
    }

    public Media(Integer id, String category, int price, int value, String title, String description, int quantity, Date importDate, Boolean rushOrderSupport, String barcode, String productDimension, String imageUrl) {
        this.setId(id);
        this.setCategory(category);
        this.setPrice(price);
        this.setValue(value);
        this.setTitle(title);
        this.setDescription(description);
        this.setQuantity(quantity);
        this.setImportDate(importDate);
        this.setRushOrderSupport(rushOrderSupport);
        this.setProductDimension(productDimension);
        this.setBarcode(barcode);
        this.setImageUrl(imageUrl);
    }

    public Media(Integer id, String category, int price, int value, String title, String description, int quantity, Date importDate, String barcode, String productDimension, String imageUrl) {
        this.setId(id);
        this.setCategory(category);
        this.setPrice(price);
        this.setValue(value);
        this.setTitle(title);
        this.setDescription(description);
        this.setQuantity(quantity);
        this.setImportDate(importDate);
        this.setProductDimension(productDimension);
        this.setBarcode(barcode);
        this.setImageUrl(imageUrl);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barCode) {
        this.barcode = barCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
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

    public String getProductDimension() {
        return productDimension;
    }

    public void setProductDimension(String productDimension) {
        this.productDimension = productDimension;
    }

}
