package com.example.bookmyticket.repos;

import com.example.bookmyticket.dao.TheaterSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheaterSeatRepository extends JpaRepository<TheaterSeat, Long> {

    List<TheaterSeat> findByTheaterId(Long theaterId);
}