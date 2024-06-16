package com.hust.ict.aims.entity.order;

import com.hust.ict.aims.entity.media.Media;

public class OrderMedia {
	public enum OrderType {
		NORMAL,
		RUSH,
	}
	
    private Media media;
    private int quantity;
    private OrderType orderType;
    

    public OrderMedia(Media media, int quantity) {
        this.setMedia(media);
        this.setQuantity(quantity);
    }

    public OrderMedia() {}

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
}
