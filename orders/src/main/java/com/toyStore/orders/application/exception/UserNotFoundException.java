package com.toyStore.orders.application.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String message){
        super(message);
    }
}
