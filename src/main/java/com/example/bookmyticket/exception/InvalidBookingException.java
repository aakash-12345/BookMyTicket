package com.example.bookmyticket.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }

}
