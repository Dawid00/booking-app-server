package com.dpap.bookingapp.users.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String username) {
        super("User with " + username + " does not exist");
    }
}
