package com.example.bookmyticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.inject.Named;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class ShowDTO {
    private Long showId;

    private LocalDate showDate;

    private String startTime;

    private Long runTime;

    private Long movieId;

    private Long theaterId;

    private String movieName;
}
