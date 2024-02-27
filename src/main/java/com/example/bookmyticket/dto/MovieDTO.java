package com.example.bookmyticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MovieDTO {
    private Long movieId;

    private String movieName;

    private Long runTime;
}
