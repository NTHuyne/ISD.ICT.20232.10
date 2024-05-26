package com.hust.ict.aims.entity.cart;

import java.util.ArrayList;
import java.util.List;

import com.hust.ict.aims.entity.media.Media;

public class Cart {
    private final List<CartMedia> lstCartMedia;
    private static Cart cartInstance;

    public static Cart getCart(){
        if(cartInstance == null) {
			cartInstance = new Cart();
		}
        return cartInstance;
    }

    public List<CartMedia> getListMedia() {
        return lstCartMedia;
    }

    private Cart(){
        this.lstCartMedia = new ArrayList<>();
    }

    public void addCartMedia(CartMedia cm){
        lstCartMedia.add(cm);
    }

    public void removeCartMedia(CartMedia cm){
        lstCartMedia.remove(cm);
    }

    public int getTotalMedia(){
        int total = 0;
        for (CartMedia obj : lstCartMedia) {
            total += obj.getQuantity();
        }
        return total;
    }

    public CartMedia checkMediaInCart(Media media){
        for (CartMedia cartMedia : lstCartMedia) {
            if (cartMedia.getMedia().getId() == media.getId()) {
				return cartMedia;
			}
        }
        return null;
    }
}
