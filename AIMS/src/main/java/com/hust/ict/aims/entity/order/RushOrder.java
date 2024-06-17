package com.hust.ict.aims.entity.order;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class RushOrder extends Order {
	public static final ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    private ZonedDateTime deliveryTime;
    private String instruction;
	
	public RushOrder(Order oldOrder, ZonedDateTime deliveryTime, String instruction) {
		super(oldOrder);
		this.deliveryTime = deliveryTime;
		this.instruction = instruction;
	}

	public ZonedDateTime getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(ZonedDateTime deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
    
    
}
