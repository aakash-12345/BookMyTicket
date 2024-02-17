package com.example.bookmyticket.dto;

import com.example.bookmyticket.model.Movie;
import com.example.bookmyticket.model.Show;
import com.example.bookmyticket.model.Theater;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ShowDTO {
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private Movie movie;
    private Theater theater;

    public ShowDTO(Show show) {
        this.id=show.getId();
        this.date=show.getDate();
        this.startTime=show.getStartTime();
        this.movie=show.getMovie();
    }
}
