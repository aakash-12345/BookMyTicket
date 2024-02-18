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
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private Movie movie;
    private Theater theater;
}
