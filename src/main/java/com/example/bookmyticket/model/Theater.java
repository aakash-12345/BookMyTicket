package com.example.bookmyticket.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "theater")
@Data
@Builder
@RequiredArgsConstructor
public class Theater {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "theater_seq")
    @SequenceGenerator(name = "theater_seq", sequenceName = "theater_seq", initialValue = 1, allocationSize = 1)
    private Long theaterId;

    private String theaterName;
    private String theaterCity;

}