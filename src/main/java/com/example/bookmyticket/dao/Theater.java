package com.example.bookmyticket.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "theater")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Theater {
    @Id
    @Column(name = "THEATER_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "theater_seq")
    @SequenceGenerator(name = "theater_seq", sequenceName = "theater_seq", initialValue = 1, allocationSize = 1)
    private Long theaterId;

    private String theaterName;
    private String theaterCity;

}