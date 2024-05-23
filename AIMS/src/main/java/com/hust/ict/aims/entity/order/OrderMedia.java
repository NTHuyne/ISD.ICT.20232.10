package com.hust.ict.aims.entity.order;

import com.hust.ict.aims.entity.media.Media;

public class OrderMedia {
    private Media media;
    private int price;
    private int quantity;

    public OrderMedia(Media media, int price, int quantity) {
        this.setMedia(media);
        this.setPrice(price);
        this.setQuantity(quantity);
    }

    public OrderMedia() {}

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
