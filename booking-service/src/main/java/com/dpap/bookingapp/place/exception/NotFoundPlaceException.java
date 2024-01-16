package com.dpap.bookingapp.place.exception;

public class NotFoundPlaceException extends RuntimeException {
    public NotFoundPlaceException(String message) {
        super(message);
    }
}
