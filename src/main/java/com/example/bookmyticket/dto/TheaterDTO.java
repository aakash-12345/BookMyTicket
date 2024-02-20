package com.example.bookmyticket.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TheaterDTO {

    private Long theaterId;
    private String theaterName;
    private String theaterCity;
}
