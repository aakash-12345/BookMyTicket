package com.example.bookmyticket.repos;

import com.example.bookmyticket.dao.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    Optional<Theater> findByTheaterNameAndTheaterCity(String theaterName, String theaterCity);
}