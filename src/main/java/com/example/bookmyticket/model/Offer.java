package com.example.bookmyticket.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "offer")
@Data
@Builder
@RequiredArgsConstructor
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offer_seq")
    @SequenceGenerator(name = "offer_seq", sequenceName = "offer_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long offerId;
    private String offerName;
    private BigDecimal offerDiscount;
    private LocalDate offerStartDate;
    private LocalDate offerEndDate;

}