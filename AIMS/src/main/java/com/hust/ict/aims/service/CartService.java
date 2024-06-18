package com.hust.ict.aims.service;

import java.util.List;

import com.hust.ict.aims.entity.cart.Cart;
import com.hust.ict.aims.entity.cart.CartMedia;

public class CartService {
	private List<CartMedia> lstCartMedia;

    public CartService() {
        this.lstCartMedia = Cart.getCart().getListMedia();
    }

    // functional cohesion
    public void checkAvailabilityOfProduct() {
        boolean check = true;
        for (CartMedia object : lstCartMedia) {
            CartMedia cartMedia = object;
            int requiredQuantity = cartMedia.getQuantity();
            int availQuantity = cartMedia.getMedia().getTotalQuantity();
            if (requiredQuantity > availQuantity) {
				check = false;
			}
        }
        if(!check) {
			throw new RuntimeException("Some media not available");
		}
    }

    // functional cohesion
    public int calSubtotal(){
        int total = 0;
        for (Object obj : lstCartMedia) {
            CartMedia cm = (CartMedia) obj;
            total += cm.getPrice()*cm.getQuantity();
        }
        return total;
    }

    public List<CartMedia> getListMedia(){
        return lstCartMedia;
    }
}
