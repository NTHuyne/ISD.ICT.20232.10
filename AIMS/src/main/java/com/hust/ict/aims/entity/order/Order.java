package com.hust.ict.aims.entity.order;

import java.util.ArrayList;
import java.util.List;

import com.hust.ict.aims.entity.shipping.DeliveryInfo;

public class Order {
	public enum OrderStatus {
		PENDING,
		REJECTED,
		ACCEPTED
	}
	  
    private int id;
    private int shippingFees;
    private int subtotal;
    private OrderStatus status = OrderStatus.PENDING;
    private DeliveryInfo deliveryInfo;
    private List<OrderMedia> lstOrderMedia = new ArrayList<>();

	public Order(int id, int shippingFees, int subtotal, OrderStatus status, DeliveryInfo deliveryInfo,
			List<OrderMedia> lstOrderMedia) {
		super();
		this.id = id;
		this.shippingFees = shippingFees;
		this.subtotal = subtotal;
		this.status = status;
		this.deliveryInfo = deliveryInfo;
		this.lstOrderMedia = lstOrderMedia;
	}

	public Order(Order oldOrder) {
		super();
		this.id = oldOrder.getId();
		this.shippingFees = oldOrder.getShippingFees();
		this.subtotal = oldOrder.getSubtotal();
		this.status = oldOrder.getStatus();
		this.deliveryInfo = oldOrder.getDeliveryInfo();
		this.lstOrderMedia = oldOrder.lstOrderMedia;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShippingFees() {
        return shippingFees;
    }

    public void setShippingFees(int shippingFees) {
        this.shippingFees = shippingFees;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public List<OrderMedia> getLstOrderMedia() {
        return lstOrderMedia;
    }

    public void setLstOrderMedia(List<OrderMedia> lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public DeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
	public String getTypeName() {
		return "Normal order";
	}
}
