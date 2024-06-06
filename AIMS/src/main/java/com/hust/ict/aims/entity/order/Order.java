package com.hust.ict.aims.entity.order;

import com.hust.ict.aims.entity.shipping.DeliveryInfo;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private int shippingFees;
    private int subtotal;
    private List<OrderMedia> lstOrderMedia = new ArrayList<>();
    private DeliveryInfo deliveryInfo;
    private boolean isRushOrder;
    private LocalDate localDate;
    private LocalTime localTime;

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

    public boolean getIsRushOrder() { return isRushOrder; }

    public void setIsRushOrder(boolean isRushOrder) { this.isRushOrder = isRushOrder; }

    public LocalDate getLocalDate() { return this.localDate; }

    public void setLocalDate(LocalDate localDate) { this.localDate = localDate; }

    public LocalTime getLocalTime() { return this.localTime; }

    public void setLocalTime(LocalTime localTime) { this.localTime = localTime; }
}
