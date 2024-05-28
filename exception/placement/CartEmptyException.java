package com.hust.ict.aims.exception.placement;

public class CartEmptyException extends RuntimeException{
    private String msg;

    public CartEmptyException() {

    }

    public CartEmptyException(String msg) {
        setMsg(msg);
        System.err.println(msg);
    }

    public void setMsg(String msg) { this.msg = msg; }

    public String getMsg() { return msg; }
}
