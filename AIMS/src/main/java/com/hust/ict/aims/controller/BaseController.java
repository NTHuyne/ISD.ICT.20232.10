package com.hust.ict.aims.controller;

import java.util.List;

import com.hust.ict.aims.entity.cart.Cart;
import com.hust.ict.aims.entity.cart.CartMedia;
import com.hust.ict.aims.entity.media.Media;

public class BaseController {

    // Coincidental cohesion
    public CartMedia checkMediaInCart(Media media){
        return Cart.getCart().checkMediaInCart(media);
    }

    // Coincidental cohesion
    public List<CartMedia> getListCartMedia(){
        return Cart.getCart().getListMedia();
    }
}
