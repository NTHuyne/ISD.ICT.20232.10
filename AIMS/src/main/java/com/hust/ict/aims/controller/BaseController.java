package com.hust.ict.aims.controller;

import com.hust.ict.aims.entity.cart.Cart;
import com.hust.ict.aims.entity.cart.CartMedia;
import com.hust.ict.aims.entity.media.Media;

import java.util.List;

public class BaseController {

    // Coincidental cohesion
    public CartMedia checkMediaInCart(Media media){
        return Cart.getCart().checkMediaInCart(media);
    }

    // Coincidental cohesion
    public List getListCartMedia(){
        return Cart.getCart().getListMedia();
    }
}
