package com.example.bookmyticket.data;

import com.example.bookmyticket.model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<ShowSeat> findAllByShowIdAndStatus(Long showId, ShowSeat.BookingStatus status);
    List<ShowSeat> findAllByShowSeatIdIn(List<Long> showSeats);
    List<ShowSeat> findAllByStatus(ShowSeat.BookingStatus status);
}