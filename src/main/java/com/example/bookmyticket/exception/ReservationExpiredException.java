package com.example.bookmyticket.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReservationExpiredException extends Exception {
    public ReservationExpiredException(String message) {
        super(message);
    }

}
