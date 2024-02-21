package com.example.bookmyticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
public class TheaterSeatDTO {
    private Long theaterSeatId;
    private Long theaterId;
    private String seatType;
    private BigDecimal seatPrice;
}
