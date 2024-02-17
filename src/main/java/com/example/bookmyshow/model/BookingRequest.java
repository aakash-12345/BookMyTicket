package com.example.bookmyshow.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookingRequest {
    public Long showId;
    public List<Long> seats;
    public Long customerId;

}
