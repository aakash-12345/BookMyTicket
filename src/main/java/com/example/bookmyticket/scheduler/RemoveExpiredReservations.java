package com.example.bookmyticket.scheduler;

import com.example.bookmyticket.dao.Booking;
import com.example.bookmyticket.dao.ShowSeat;
import com.example.bookmyticket.repos.BookingRepository;
import com.example.bookmyticket.repos.ShowSeatRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Log4j2
@RequiredArgsConstructor
public class RemoveExpiredReservations {

    @Getter
    @Value("${payment.session.timeout}")
    private Integer sessionTimeout = 300000;

    private final ShowSeatRepository showSeatRepository;

    private final BookingRepository bookingRepository;

    @Transactional
    @Scheduled(fixedRateString = "${polling.frequency}")
    public void removeAllExpiredReservations() {
        log.info("Removing expired reservations scheduler started");
        List<ShowSeat> pendingShowSeats = showSeatRepository.findAllByStatus(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING);
        if (!CollectionUtils.isEmpty(pendingShowSeats)) {
            for (ShowSeat showSeat : pendingShowSeats) {
                if (Duration.between(showSeat.getReservationTime(), LocalDateTime.now()).toMillis() > getSessionTimeout()) {
                    log.info("Removing expired reservation for showSeatId: {} and bookingId: {}",
                            showSeat.getShowSeatId(), showSeat.getBookingId());
                    Booking booking = bookingRepository.findById(showSeat.getBookingId()).get();
                    showSeat.setStatus(ShowSeat.BookingStatus.UNRESERVED);
                    showSeat.setBookingId(null);
                    showSeat.setReservationTime(null);
                    showSeatRepository.save(showSeat);
                    booking.setIsCancelled(true);
                    bookingRepository.save(booking);
                    log.info("Expired reservation removed successfully");
                }
            }
        }
        log.info("Expired reservations scheduler ended");
    }
}
