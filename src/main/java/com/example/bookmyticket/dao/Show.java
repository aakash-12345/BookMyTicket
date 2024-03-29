package com.example.bookmyticket.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "show")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Show {
    @Id
    @Column(name = "SHOW_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "show_seq")
    @SequenceGenerator(name = "show_seq", sequenceName = "show_seq", initialValue = 1, allocationSize = 1)
    private Long showId;

    private LocalDate showDate;

    private String startTime;

    private Long movieId;

    private Long theaterId;

}