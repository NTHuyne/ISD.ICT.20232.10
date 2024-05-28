package com.hust.ict.aims.entity.order;

import com.hust.ict.aims.entity.shipping.DeliveryInfo;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private int shippingFees;
    private int subtotal;
    private List<OrderMedia> lstOrderMedia = new ArrayList<>();
    private DeliveryInfo deliveryInfo;

    public Order(){
    }

    public Order(int id, int shippingFees, int subtotal) {
        this.setId(id);
        this.setShippingFees(shippingFees);
        this.setSubtotal(subtotal);
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
}
