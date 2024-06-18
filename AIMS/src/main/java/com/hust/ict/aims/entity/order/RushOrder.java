package com.hust.ict.aims.entity.order;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class RushOrder extends Order {
    private Instant deliveryTime;
    private String instruction;
	
	public RushOrder(Order oldOrder, Instant deliveryTime, String instruction) {
		super(oldOrder);
		this.deliveryTime = deliveryTime;
		this.instruction = instruction;
	}

	public String getDeliveryTimeAsString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
		return formatter.format(deliveryTime);
	}
	public Instant getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Instant deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
    
    
}
