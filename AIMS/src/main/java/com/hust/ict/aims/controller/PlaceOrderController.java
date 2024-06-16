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
import javafx.scene.control.TextField;

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

//    public Order categorizeRegularOrder(Order order) {
//        List<OrderMedia> lstRegularOrderMedia = new ArrayList<>();
//        for(OrderMedia orderMedia : order.getLstOrderMedia()) {
//            if(!orderMedia.getMedia().isRushOrderSupported()) {
//				lstRegularOrderMedia.add(orderMedia);
//			}
//        }
//
//        Order regularOrder = new Order();
//        regularOrder.setDeliveryInfo(order.getDeliveryInfo());
//        regularOrder.setLstOrderMedia(lstRegularOrderMedia);
//        regularOrder = calculateSubTotal(regularOrder);
//        regularOrder.setShippingFees(calculateShippingFee(regularOrder));
//        regularOrder.setIsRushOrder(false);
//        return regularOrder;
//    }

    public boolean canPlaceRushOrder(Order order) {
        for(OrderMedia orderMedia : order.getLstOrderMedia()) {
            if(orderMedia.getMedia().isRushOrderSupported()) {
				return true;
			}
        }
        return false;
    }

    public void categorizeOrder(Order order) {
        List<OrderMedia> lstOrderMedia = new ArrayList<>();
        for(OrderMedia orderMedia : order.getLstOrderMedia()) {
            if(orderMedia.getMedia().isRushOrderSupported()) {
                orderMedia.setOrderType(OrderMedia.OrderType.RUSH);
            }
            else {
                orderMedia.setOrderType(OrderMedia.OrderType.NORMAL);
            }
        }
    }

    public int calculateSubTotal(List<OrderMedia> lstOrderMedia) {
        int subTotal = 0;
        for(OrderMedia orderMedia : lstOrderMedia) {
            subTotal += (orderMedia.getMedia().getPrice() * orderMedia.getQuantity());
        }
        return subTotal;
    }

    public int calculateVAT(Order order) {
        return calculateSubTotal(order.getLstOrderMedia()) / 10;
    }

    public int calculateShippingFee(List<OrderMedia> lstOrderMedia, OrderMedia.OrderType orderType, String province) {
        int shipFee = 0;
        double highest = 0.0;
        int subtotal = calculateSubTotal(lstOrderMedia);
        if(lstOrderMedia.isEmpty()) {
			return 0;
		}
        for(OrderMedia orderMedia : lstOrderMedia) {
            // String dimension = orderMedia.getMedia().getProductDimension();
            // String[] dimensions = dimension.split("x");
            // Density of each item is assumed to be 5000 kg/m3
            // float mass = Float.valueOf(dimensions[0]) * Float.valueOf(dimensions[1]) * Float.valueOf(dimensions[2]) * 0.0006f;
        	
            // Note 11/6/24: Now has weight for media
            highest = Math.max(highest, orderMedia.getMedia().getWeight());
        }
        if(province.equals("Hà Nội") || province.equals("Hồ Chí Minh")) {
            shipFee = 22000;
            if(highest > 3.0f) {
				shipFee += ((int)Math.ceil((highest - 3)/0.5) * 2500);
			}
        }
        else {
            shipFee = 30000;
            if(highest > 0.5f) {
				shipFee += ((int)Math.ceil((highest - 0.5)/0.5) * 2500);
			}
        }
        if(orderType == OrderMedia.OrderType.RUSH) {
			shipFee += (lstOrderMedia.size() * 10000);
		} else
        if(subtotal > 100000) {
			shipFee -= 25000;
		}
        if(shipFee < 0) {
			shipFee = 0;
		}
        return shipFee;
    }

    public Order createOrder(DeliveryInfo deliveryInfo) {
        Order order = new Order();
        order.setDeliveryInfo(deliveryInfo);
        for(Object obj : cartService.getListMedia()) {
            CartMedia cartMedia = (CartMedia) obj;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), cartMedia.getQuantity());
            order.getLstOrderMedia().add(orderMedia);
        }
        order.setIsRushOrder(false);
        return order;
    }

    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    public static boolean validatePhoneField(TextField phoneField) {
        if(phoneField.getText().isEmpty()) {
            return false;
        }
        String phone = phoneField.getText();
        if(phone.length() != 10) {
            return false;
        }
        if(phone.charAt(0) != '0') {
            return false;
        } else if(phone.charAt(1) == '0') {
            return false;
        }
        for(int i=1; i<phone.length(); i++) {
            if(phone.charAt(i) < 48 && phone.charAt(i) > 57) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateEmailField(TextField emailField) {
        if(emailField.getText().isEmpty()) {
            return false;
        }
        String email = emailField.getText();
        if(!email.contains("@") || (!email.contains(".com") && !email.contains(".org") && !email.contains(".edu"))) {
            return false;
        }

        // Allow only 1 @ character in email
        int count = 0;
        for(int i=0; i<email.length(); i++) {
            if(email.charAt(i) == '@') {
                count++;
            }
        }
        if(count > 1) {
            return false;
        }
        int idx_at = email.indexOf('@');
        if(idx_at == 0) {
            return false;
        }
        if(email.charAt(idx_at+1) == '.') {
            return false;
        }
        return true;
    }
}
