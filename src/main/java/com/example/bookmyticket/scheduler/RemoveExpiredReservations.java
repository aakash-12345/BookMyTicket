package com.example.bookmyticket.scheduler;

import com.example.bookmyticket.repos.BookingRepository;
import com.example.bookmyticket.repos.ShowSeatRepository;
import com.example.bookmyticket.dao.Booking;
import com.example.bookmyticket.dao.ShowSeat;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RemoveExpiredReservations {

    @Getter
    @Value("${payment.session.timeout}")
    private Integer sessionTimeout=300000;

    private final ShowSeatRepository showSeatRepository;

    private final BookingRepository bookingRepository;

    @Transactional
    @Scheduled(fixedRateString = "${polling.frequency}")
    public void removeExpiredReservations() {
        List<ShowSeat> pendingShowSeats = showSeatRepository.findAllByStatus(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING);
        if (!CollectionUtils.isEmpty(pendingShowSeats)) {
            List<Booking> bookings = new ArrayList<>();
            for (ShowSeat showSeat : pendingShowSeats) {
                if (Duration.between(showSeat.getReservationTime(), LocalDateTime.now()).toMillis() > getSessionTimeout()) {
                    Booking booking = bookingRepository.findById(showSeat.getBookingId()).get();
                    showSeat.setStatus(ShowSeat.BookingStatus.UNRESERVED);
                    showSeat.setBookingId(null);
                    showSeat.setReservationTime(null);
                    showSeatRepository.save(showSeat);
                    bookings.add(booking);
                }
            }
            bookingRepository.deleteAll(bookings);
        }

    }
}
