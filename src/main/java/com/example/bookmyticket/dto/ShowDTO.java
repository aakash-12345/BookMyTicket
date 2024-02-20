package com.example.bookmyticket.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ShowDTO {
    private Long showId;

    private LocalDate showDate;

    private String startTime;

    private Long runTime;

    private Long movieId;

    private Long theaterId;

    private String movieName;
}
