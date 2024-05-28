package com.hust.ict.aims.exception.placement;

public class RushOrderUnsupportedException extends RuntimeException {
    public RushOrderUnsupportedException() {

    }

    public RushOrderUnsupportedException(String msg) {
        System.err.println(msg);
    }
}
