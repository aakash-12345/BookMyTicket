package com.example.bookmyshow.api;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookingRequest {
    Long showId;
    List<Long> seats;
    Long customerId;

}
