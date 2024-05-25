package com.hust.ict.aims.controller;

import com.hust.ict.aims.service.CartService;
public class ViewCartController extends BaseController {
	CartService cartService = new CartService();

    public void checkAvailabilityOfProduct() {
        cartService.checkAvailabilityOfProduct();
    }
    public int getCartSubtotal(){
        int subtotal = cartService.calSubtotal();
        return subtotal;
    }
}
