package com.example.bookmyticket.data;

import com.example.bookmyticket.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
}