package com.example.bookmyticket.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ShowSeatDTOResponse {
    private Long showId;
    List<Long> availableShowSeatIDs;
}
