package com.example.bookmyticket.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Data
public class Booking {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_seq")
    @SequenceGenerator(name = "booking_seq", sequenceName = "booking_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    private BigDecimal totalAmount;

    private Long showId;

    private Long theaterId;

    private LocalDateTime reservationDate;

    private Long customerId;
}