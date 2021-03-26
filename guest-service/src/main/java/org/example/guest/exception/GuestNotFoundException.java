package org.example.guest.exception;

public class GuestNotFoundException extends RuntimeException {
    public GuestNotFoundException(String msg) {
        super(msg);
    }
}
