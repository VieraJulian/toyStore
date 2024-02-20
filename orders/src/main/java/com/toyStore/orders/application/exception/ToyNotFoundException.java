package com.toyStore.orders.application.exception;

public class ToyNotFoundException extends Exception {

    public ToyNotFoundException(String message){
        super(message);
    }
}
