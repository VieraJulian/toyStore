package com.toyStore.users.application.exception;

public class UserIsExistsException extends Exception {

    public UserIsExistsException(String message){
        super(message);
    }
}
