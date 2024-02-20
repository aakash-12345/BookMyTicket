package com.example.bookmyticket.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieDTO {
    private Long movieId;

    private String movieName;
}
