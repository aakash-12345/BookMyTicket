package com.example.bookmyticket.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "theater_seat")
@Data
@Builder
@RequiredArgsConstructor
public class TheaterSeat {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "theaterseat_seq")
    @SequenceGenerator(name = "theaterseat_seq", sequenceName = "theaterseat_seq", initialValue = 1, allocationSize = 1)
    private Long theaterSeatId;
    private Long theaterId;
    private String seatType;
    private BigDecimal seatPrice;

}