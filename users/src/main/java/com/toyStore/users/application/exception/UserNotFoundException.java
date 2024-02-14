package com.toyStore.users.application.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String message){
        super(message);
    }
}
