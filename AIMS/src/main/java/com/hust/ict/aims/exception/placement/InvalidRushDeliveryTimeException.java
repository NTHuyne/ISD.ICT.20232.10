package com.hust.ict.aims.exception.placement;

public class InvalidRushDeliveryTimeException extends RuntimeException{
    private String msg;

    public InvalidRushDeliveryTimeException() {

    }

    public InvalidRushDeliveryTimeException(String msg) {
        setMsg(msg);
        System.err.println(msg);
    }

    public void setMsg(String msg) { this.msg = msg; }

    public String getMsg() { return msg; }
}
