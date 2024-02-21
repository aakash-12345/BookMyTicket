package com.example.bookmyticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class TheaterDTO {

    private Long theaterId;
    private String theaterName;
    private String theaterCity;
}
