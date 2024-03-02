package com.example.bookmyticket.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String message) {
        super(message);
    }

}
