package com.hust.ict.aims.controller;

import com.hust.ict.aims.entity.cart.Cart;
import com.hust.ict.aims.entity.cart.CartMedia;
import com.hust.ict.aims.entity.invoice.Invoice;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.order.OrderMedia;
import com.hust.ict.aims.entity.shipping.DeliveryInfo;
import com.hust.ict.aims.exception.placement.InsufficientProductException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.hust.ict.aims.service.CartService;

import java.util.List;

public class PlaceOrderController extends BaseController{
    private CartService cartService;
    private Cart cart;
    private DeliveryInfo deliveryInfo;
    private Order regularOrder;
    private Order rushOrder;

    public PlaceOrderController() {
        this.cartService = new CartService();
    }

    public void placeOrder() {
        try{
            cartService.checkAvailabilityOfProduct();
        }
        catch(Exception e) {
            // Display alert
            e.printStackTrace();
        }
    }

    public void placeRushOrder() {

    }

    public Order createOrder(DeliveryInfo deliveryInfo) {
//        this.deliveryInfo = deliveryInfo;
        Order order = new Order();
        order.setDeliveryInfo(deliveryInfo);
        System.out.println("Create Order, length cart items="+cartService.getListMedia().size());
        for(Object obj : cartService.getListMedia()) {
            CartMedia cartMedia = (CartMedia) obj;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), cartMedia.getPrice(),
                    cartMedia.getQuantity());
            order.getLstOrderMedia().add(orderMedia);
        }
        System.out.println("Length of order items="+order.getLstOrderMedia().size());
        return order;
    }

    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }
}
