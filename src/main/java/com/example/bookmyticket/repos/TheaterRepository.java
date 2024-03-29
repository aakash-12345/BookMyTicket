package com.example.bookmyticket.repos;

import com.example.bookmyticket.dao.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    Optional<Theater> findByTheaterNameAndTheaterCity(String theaterName, String theaterCity);
}