package com.example.bookmyticket.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "movie")
@Data
@Builder
@RequiredArgsConstructor
public class Movie {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_seq")
    @SequenceGenerator(name = "movie_seq", sequenceName = "movie_seq", initialValue = 1, allocationSize = 1)
    private Long movieId;

    private String movieName;
}