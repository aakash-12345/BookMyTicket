package com.example.bookmyticket.repos;

import com.example.bookmyticket.dao.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<ShowSeat> findAllByShowIdAndStatus(Long showId, ShowSeat.BookingStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "0")})
    List<ShowSeat> findAllByShowSeatIdIn(List<Long> showSeats);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "0")})
    List<ShowSeat> findAllByShowSeatIdInAndStatus(List<Long> showSeats, ShowSeat.BookingStatus status);

    List<ShowSeat> findAllByStatus(ShowSeat.BookingStatus status);
}