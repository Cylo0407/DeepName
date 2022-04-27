package com.example.deepname.Exception;

public class BussinessException extends RuntimeException{
    public BussinessException() {
        super();
    }

    public BussinessException(String msg) {
        super(msg);
    }
}
