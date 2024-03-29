package com.example.bookmyticket.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @Column(name = "BOOKING_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_seq")
    @SequenceGenerator(name = "booking_seq", sequenceName = "booking_seq", initialValue = 1, allocationSize = 1)
    private Long bookingId;
    private BigDecimal totalAmount;
    private Long showId;
    private Long theaterId;
    private LocalDateTime reservationDate;
    private Long customerId;
    private Boolean isCancelled;
}