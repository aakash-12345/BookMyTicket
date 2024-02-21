package com.example.bookmyticket.repos;

import com.example.bookmyticket.dao.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}