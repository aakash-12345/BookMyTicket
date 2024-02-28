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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    public void removeAllExpiredReservations() {

        ShowSeat pendingShowSeat1 = ShowSeat.builder()
                .showSeatId(1L)
                .showId(1L)
                .theaterSeatId(1L)
                .reservationTime(LocalDateTime.now())
                .bookingId(1L)
                .status(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING)
                .build();
        ShowSeat pendingShowSeat2 = ShowSeat.builder()
                .showSeatId(2L)
                .showId(1L)
                .theaterSeatId(2L)
                .reservationTime(LocalDateTime.now().minusMinutes(10))
                .bookingId(1L)
                .status(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING)
                .build();
        List<ShowSeat> pendingShowSeats = new ArrayList<>();
        pendingShowSeats.add(pendingShowSeat1);
        pendingShowSeats.add(pendingShowSeat2);

        when(showSeatRepository.findAllByStatus(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING))
                .thenReturn(pendingShowSeats);
        Booking booking = new Booking();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        removeExpiredReservationsObject.removeAllExpiredReservations();

        verify(showSeatRepository, times(1)).save(any());

        assertEquals(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING, pendingShowSeat1.getStatus());
        assertEquals(ShowSeat.BookingStatus.UNRESERVED, pendingShowSeat2.getStatus());
    }
}
