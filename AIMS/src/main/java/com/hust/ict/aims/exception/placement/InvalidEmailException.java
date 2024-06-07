package com.hust.ict.aims.exception.placement;

public class InvalidEmailException extends RuntimeException{
    private String msg;

    public InvalidEmailException() {

    }

    public InvalidEmailException(String msg) {
        setMsg(msg);
        System.err.println(msg);
    }

    public void setMsg(String msg) { this.msg = msg; }

    public String getMsg() { return msg; }
}
