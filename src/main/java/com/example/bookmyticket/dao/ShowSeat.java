package com.example.bookmyticket.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "show_seat")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowSeat {
    @Id
    @Column(name = "SHOW_SEAT_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "showseat_seq")
    @SequenceGenerator(name = "showseat_seq", sequenceName = "showseat_seq", initialValue = 1, allocationSize = 1)
    private Long showSeatId;

    private Long showId;

    private LocalDateTime reservationTime;

    private Long theaterSeatId;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.UNRESERVED;

    private Long bookingId;

    public enum BookingStatus {
        UNRESERVED, RESERVED_PAYMENT_PENDING, CONFIRMED
    }
}