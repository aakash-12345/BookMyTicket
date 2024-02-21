package com.example.bookmyticket.repos;

import com.example.bookmyticket.dao.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    List<Theater> findAllByTheaterNameAndTheaterCity(String theaterName, String theaterCity);
}