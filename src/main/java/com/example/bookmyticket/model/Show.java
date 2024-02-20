package com.example.bookmyticket.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "show", uniqueConstraints = {@UniqueConstraint(columnNames = {"date", "movie_id", "theater_id"})})
@Data
@Builder
@RequiredArgsConstructor
public class Show {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "show_seq")
    @SequenceGenerator(name = "show_seq", sequenceName = "show_seq", initialValue = 1, allocationSize = 1)
    private Long showId;

    private LocalDate showDate;

    private String startTime;

    private Long runTime;

    private Long movieId;

    private Long theaterId;

}