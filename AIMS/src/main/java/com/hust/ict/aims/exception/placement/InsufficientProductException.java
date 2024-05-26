package com.hust.ict.aims.exception.placement;

public class InsufficientProductException extends RuntimeException{
    public InsufficientProductException() {

    }

    public InsufficientProductException(String msg) {
        System.out.println(msg);
    }
}
