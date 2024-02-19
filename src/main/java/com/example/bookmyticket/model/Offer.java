package com.example.bookmyticket.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "offer")
@Data
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offer_seq")
    @SequenceGenerator(name = "offer_seq", sequenceName = "offer_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "offers")
    private List<Theater> theaters = new java.util.ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OfferType offerType;

}