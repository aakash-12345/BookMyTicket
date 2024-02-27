package com.example.bookmyticket.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SeatUnavailableException extends Exception {
    public SeatUnavailableException(String message) {
        super(message);
    }

}
