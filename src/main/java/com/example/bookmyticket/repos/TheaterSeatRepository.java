package com.example.bookmyticket.repos;

import com.example.bookmyticket.dao.TheaterSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterSeatRepository extends JpaRepository<TheaterSeat, Long> {

    List<TheaterSeat> findByTheaterId(Long theaterId);
}