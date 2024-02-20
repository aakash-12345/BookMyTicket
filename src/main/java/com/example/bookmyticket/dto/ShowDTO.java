package com.example.bookmyticket.dto;

import com.example.bookmyticket.model.Movie;
import com.example.bookmyticket.model.Theater;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

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
