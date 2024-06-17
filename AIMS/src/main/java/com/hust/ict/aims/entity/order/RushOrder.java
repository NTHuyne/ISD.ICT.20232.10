package com.hust.ict.aims.entity.order;

import java.time.LocalTime;

public class RushOrder extends Order {
    private LocalTime deliveryTime;
    private String instruction;
	
	public RushOrder(Order oldOrder, LocalTime deliveryTime, String instruction) {
		super(oldOrder);
		this.deliveryTime = deliveryTime;
		this.instruction = instruction;
	}

	public LocalTime getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(LocalTime deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
    
    
}
