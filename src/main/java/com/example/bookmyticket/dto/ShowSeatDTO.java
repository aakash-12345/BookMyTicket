package com.example.bookmyticket.dto;

import com.example.bookmyticket.model.Booking;
import com.example.bookmyticket.model.ShowSeat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ShowSeatDTO {
    private Long id;
    private Long showid;
    private LocalDateTime reservationTime;
    private ShowSeat.BookingStatus status;
    private Booking booking;
}
