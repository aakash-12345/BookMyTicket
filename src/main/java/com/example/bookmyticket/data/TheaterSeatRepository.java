package com.example.bookmyticket.data;

import com.example.bookmyticket.model.Theater;
import com.example.bookmyticket.model.TheaterSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheaterSeatRepository extends JpaRepository<TheaterSeat, Long> {

    List<TheaterSeat> findByTheaterId(Long theaterId);
}