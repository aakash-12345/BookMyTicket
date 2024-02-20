package com.example.bookmyticket.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowSeatDTO {
    private Long showSeatId;
    private Long showId;
}
