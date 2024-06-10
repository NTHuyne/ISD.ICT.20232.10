package com.hust.ict.aims.controller;

import java.util.ArrayList;
import java.util.List;

import com.hust.ict.aims.entity.cart.Cart;
import com.hust.ict.aims.entity.cart.CartMedia;
import com.hust.ict.aims.entity.invoice.Invoice;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.order.OrderMedia;
import com.hust.ict.aims.entity.shipping.DeliveryInfo;
import com.hust.ict.aims.exception.placement.RushOrderUnsupportedException;
import com.hust.ict.aims.service.CartService;

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

    public void placeRushOrder(Order order) {
        String province = order.getDeliveryInfo().getProvince();
        if(!province.equals("Hà Nội")) {
            throw new RushOrderUnsupportedException();
        }

        Order rushOrder = categorizeRushOrder(order);
        if(rushOrder.getLstOrderMedia().isEmpty()) {
			throw new RushOrderUnsupportedException();
		}
    }

    public Order categorizeRegularOrder(Order order) {
        List<OrderMedia> lstRegularOrderMedia = new ArrayList<>();
        for(OrderMedia orderMedia : order.getLstOrderMedia()) {
            if(!orderMedia.getMedia().isRushOrderSupported()) {
				lstRegularOrderMedia.add(orderMedia);
			}
        }

        Order regularOrder = new Order();
        regularOrder.setDeliveryInfo(order.getDeliveryInfo());
        regularOrder.setLstOrderMedia(lstRegularOrderMedia);
        regularOrder = calculateSubTotal(regularOrder);
        regularOrder.setShippingFees(calculateShippingFee(regularOrder));
        regularOrder.setIsRushOrder(false);
        return regularOrder;
    }

    public Order categorizeRushOrder(Order order) {
        List<OrderMedia> lstRushOrderMedia = new ArrayList<>();
        for(OrderMedia orderMedia : order.getLstOrderMedia()) {
            if(orderMedia.getMedia().isRushOrderSupported()) {
				lstRushOrderMedia.add(orderMedia);
			}
        }

        Order rushOrder = new Order();
        rushOrder.setDeliveryInfo(order.getDeliveryInfo());
        rushOrder.setLstOrderMedia(lstRushOrderMedia);
        rushOrder = calculateSubTotal(rushOrder);
        rushOrder.setShippingFees(calculateShippingFee(rushOrder));
        rushOrder.setIsRushOrder(true);
        return rushOrder;
    }

    public Order calculateSubTotal(Order order) {
        List<OrderMedia> lstOrderMedia = order.getLstOrderMedia();
        int subTotal = 0;
        for(OrderMedia orderMedia : lstOrderMedia) {
            subTotal += (orderMedia.getPrice() * orderMedia.getQuantity());
        }
        order.setSubtotal(subTotal);
        return order;
    }

    public int calculateShippingFee(Order order) {
        int shipFee = 0;
        double highest = 0.0f;
        if(order.getLstOrderMedia().isEmpty()) {
			return 0;
		}
        for(OrderMedia orderMedia : order.getLstOrderMedia()) {
            // String dimension = orderMedia.getMedia().getProductDimension();
            // String[] dimensions = dimension.split("x");
            // Density of each item is assumed to be 5000 kg/m3
            // float mass = Float.valueOf(dimensions[0]) * Float.valueOf(dimensions[1]) * Float.valueOf(dimensions[2]) * 0.0006f;
        	
            // Note 11/6/24: Now has weight for media
            highest = Math.max(highest, orderMedia.getMedia().getWeight());
        }
        if(order.getDeliveryInfo().getProvince().equals("Hà Nội") || order.getDeliveryInfo().getProvince().equals("Hồ Chí Minh")) {
            shipFee = 22000;
            if(highest > 3.0f) {
				shipFee += ((int)Math.ceil((highest - 3)/0.5f) * 2500);
			}
        }
        else {
            shipFee = 30000;
            if(highest > 0.5f) {
				shipFee += ((int)Math.ceil((highest - 0.5)/0.5f) * 2500);
			}
        }
        if(order.getIsRushOrder()) {
			shipFee += (order.getLstOrderMedia().size() * 10000);
		} else
        if(order.getSubtotal() > 100000) {
			shipFee -= 25000;
		}
        if(shipFee < 0) {
			shipFee = 0;
		}
        return shipFee;
    }

    public Order createOrder(DeliveryInfo deliveryInfo) {
//        this.deliveryInfo = deliveryInfo;
        Order order = new Order();
        order.setDeliveryInfo(deliveryInfo);
        for(Object obj : cartService.getListMedia()) {
            CartMedia cartMedia = (CartMedia) obj;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), cartMedia.getPrice(),
                    cartMedia.getQuantity());
            order.getLstOrderMedia().add(orderMedia);
        }
        order.setIsRushOrder(false);
        return order;
    }

    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    public void requestToPayOrder() {

    }
}
