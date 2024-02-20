package com.example.bookmyticket.data;

import com.example.bookmyticket.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    List<Theater> findAllByTheaterNameAndTheaterCity(String theaterName, String theaterCity);
}