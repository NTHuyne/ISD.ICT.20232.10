package com.hust.ict.aims.entity.order;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.hust.ict.aims.entity.media.Media;

public class OrderMedia {
	public enum OrderType {
		NORMAL(0), RUSH(1),;

		private final int type;
		
		
		OrderType(int type) {
			this.type = type;
		}
		
		public int toInt() {
			return type;
		}
		
		private static final Map<Integer, OrderType> LOOKUP = 
				Arrays.asList(OrderType.values())
				.stream()
				.collect(Collectors.toMap(OrderType::toInt, Function.identity()));
				
		public static OrderType fromInt(int id) {
			return LOOKUP.get(id);
		}
	}
	
    private Media media;
    private int quantity;
    private OrderType orderType;
    

    public OrderMedia(Media media, int quantity, OrderType orderType) {
		super();
		this.media = media;
		this.quantity = quantity;
		this.orderType = orderType;
	}

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
