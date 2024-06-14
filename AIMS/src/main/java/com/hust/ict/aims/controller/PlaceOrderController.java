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
import com.hust.ict.aims.subsystem.email.IEmail;
import com.hust.ict.aims.subsystem.email.simplejavamail.SimplejavamailManager;
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
            subTotal += (orderMedia.getMedia().getPrice() * orderMedia.getQuantity());
        }
        order.setSubtotal(subTotal);
        return order;
    }

    public int calculateVAT(Order order) {
        return calculateSubTotal(order).getSubtotal() / 10;
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

    public void sendSuccessfulOrderMail(Invoice invoice){
        String destEmail = invoice.getOrder().getDeliveryInfo().getEmail();
        String invoiceId = String.valueOf(invoice.getOrder().getId());
        String recipientName = invoice.getOrder().getDeliveryInfo().getName();
        String phone = invoice.getOrder().getDeliveryInfo().getPhone();
        String address = invoice.getOrder().getDeliveryInfo().getAddress() + ", " + invoice.getOrder().getDeliveryInfo().getProvince();
        String totalAmount = String.valueOf(invoice.getTotalAmount()) + " VND";

        IEmail mail = new SimplejavamailManager();

        String emailContent = "AIMS GROUP-10 NOTIFICATION\n\nYour order has been placed successfully. Here is your invoice's information. Please check it.\n"
                + "Order ID: " + invoiceId + " (You can later use it to review your order in app)" + "\nRecipient's name: " + recipientName + "\nPhone number: " + phone + "\nAddress: " + address + "\nEmail: " + destEmail + "\nTotal amount: " + totalAmount
                + "\nYour order will be processed by manager later.\nPlease check your mail regularly.\n\nThank you.";

        mail.sendEmail(destEmail, emailContent, "AIMS GROUP-10 NOTIFICATION");
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
