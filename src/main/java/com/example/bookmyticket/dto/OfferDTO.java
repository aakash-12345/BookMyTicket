package com.example.bookmyticket.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class OfferDTO {
    private Long offerId;
    private String offerName;
    private BigDecimal offerDiscount;
}
