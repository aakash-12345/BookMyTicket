package com.example.bookmyticket.scheduler;

import com.example.bookmyticket.data.BookingRepository;
import com.example.bookmyticket.data.ShowSeatRepository;
import com.example.bookmyticket.model.Booking;
import com.example.bookmyticket.model.ShowSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RemoveExpiredReservations {

    @Value("${payment.session.timeout}")
    private Integer sessionTimeout;

    private final ShowSeatRepository showSeatRepository;

    private final BookingRepository bookingRepository;

    @Transactional
    @Scheduled(fixedRateString = "${polling.frequency}")
    public void removeExpiredReservations() {
        //fetch all PENDING ShowSeats
        List<ShowSeat> pendingShowSeats = showSeatRepository.findAllByStatus(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING);
        if (!CollectionUtils.isEmpty(pendingShowSeats)) {
            //if now() - reservationTime > SESSION_TIMEOUT then status="" and delete Booking
            List<Booking> bookings = new ArrayList<>();
            for (ShowSeat showSeat : pendingShowSeats) {
                if (Duration.between(showSeat.getReservationTime(), LocalDateTime.now()).toMillis() > sessionTimeout) {
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
