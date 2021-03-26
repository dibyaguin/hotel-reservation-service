package org.example.guest.exception;

public class BookingNotAvailableException extends RuntimeException {
    public BookingNotAvailableException(String msg) {
        super(msg);
    }
}
