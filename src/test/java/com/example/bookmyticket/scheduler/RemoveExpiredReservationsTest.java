package com.example.bookmyticket.scheduler;

import com.example.bookmyticket.dao.Booking;
import com.example.bookmyticket.dao.ShowSeat;
import com.example.bookmyticket.repos.BookingRepository;
import com.example.bookmyticket.repos.ShowSeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class RemoveExpiredReservationsTest {
    @Mock
    private ShowSeatRepository showSeatRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private RemoveExpiredReservations removeExpiredReservationsObject;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Value("${payment.session.timeout}")
    private Integer sessionTimeout = 300000;

    // Assuming getSessionTimeout() is a method in the same class
    private long getSessionTimeout() {
        // Implement getSessionTimeout() as per your requirement
        return this.sessionTimeout; // Example session timeout in milliseconds
    }

    @Test
    public void testRemoveExpiredReservations() {
        // Create some pending show seats for testing
        ShowSeat pendingShowSeat1 = ShowSeat.builder()
                .showSeatId(1L)
                .showId(1L)
                .theaterSeatId(1L)
                .reservationTime(LocalDateTime.now())
                .bookingId(1L)
                .status(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING)
                .build(); // Create pending show seat 1
        ShowSeat pendingShowSeat2 = ShowSeat.builder()
                .showSeatId(2L)
                .showId(1L)
                .theaterSeatId(2L)
                .reservationTime(LocalDateTime.now().minusMinutes(10))
                .bookingId(1L)
                .status(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING)
                .build(); // Create pending show seat 2
        List<ShowSeat> pendingShowSeats = new ArrayList<>();
        pendingShowSeats.add(pendingShowSeat1);
        pendingShowSeats.add(pendingShowSeat2);

        // Mock the behavior of showSeatRepository
        when(showSeatRepository.findAllByStatus(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING))
                .thenReturn(pendingShowSeats);
        Booking booking = new Booking();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // Mock the behavior of bookingRepository
        // ... (if needed)

        // Mock the behavior of getSessionTimeout()
//        when(removeExpiredReservationsObject.getSessionTimeout()).thenReturn(sessionTimeout); // Example session timeout for testing

        // Call the method to be tested
        removeExpiredReservationsObject.removeExpiredReservations();

        // Verify the expected behavior, for example:
        // Verify that save method is called on showSeatRepository for each expired show seat
        verify(showSeatRepository, times(1)).save(any());
        // Verify that deleteAll method is called on bookingRepository with the expected list of bookings
        verify(bookingRepository).deleteAll(anyList());

        // Add assertions as per your specific requirements
        // For example, assert that the show seat status is updated correctly
        assertEquals(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING, pendingShowSeat1.getStatus());
        assertEquals(ShowSeat.BookingStatus.UNRESERVED, pendingShowSeat2.getStatus());
    }
}
