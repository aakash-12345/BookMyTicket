package com.example.bookmyticket.repos;

import com.example.bookmyticket.dao.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    @Query("select Show from Show where theaterId = :theaterId and showDate between :currDate and :lastAvailableDate")
    List<Show> findAllShowsInRange(Long theaterId, LocalDate currDate, LocalDate lastAvailableDate);

}