package com.example.bookmyticket.model;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {
    public Long showId;
    public List<Long> seats;
    public Long customerId;

}
