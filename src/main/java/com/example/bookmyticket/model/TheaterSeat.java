package com.example.bookmyticket.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "theater_seat")
@Data
public class TheaterSeat {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "theaterseat_seq")
    @SequenceGenerator(name = "theaterseat_seq", sequenceName = "theaterseat_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;
    private String seatType;
    private BigDecimal seatPrice;

}