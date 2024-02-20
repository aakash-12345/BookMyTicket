package com.example.bookmyticket.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "offer")
@Data
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