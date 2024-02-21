package com.example.bookmyticket.api;

import com.example.bookmyticket.model.BookingRequest;
import com.example.bookmyticket.service.BookMyTicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BookMyTicketControllerTest {
    @Mock
    private BookMyTicketService bookMyTicketService;

    @InjectMocks
    private BookMyTicketController bookMyTicketController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMovieShows() {
        String theaterName = "theater 1";
        String city = "city 1";
        // When
        bookMyTicketController.movieShows(theaterName, city);

        // Then
        verify(bookMyTicketService, times(1)).findAllShowsByTheaterNameAndCity(theaterName, city);

    }

    @Test
    public void testShowSeats() {
        Long showId = 1L;
        // When
        bookMyTicketController.showSeats(showId);

        // Then
        verify(bookMyTicketService, times(1)).findAllAvailableSeatsForShow(showId);

    }

    @Test
    public void testReserveSeats() {
        BookingRequest bookingRequest = BookingRequest.builder()
                .showId(1L)
                .seats(new ArrayList<>())
                .customerId(1L).build();
        // When
        bookMyTicketController.reserveSeats(bookingRequest);

        // Then
        verify(bookMyTicketService, times(1)).reserveSeats(bookingRequest);

    }

    @Test
    public void testConfirmSeats() {
        BookingRequest bookingRequest = BookingRequest.builder()
                .showId(1L)
                .seats(new ArrayList<>())
                .customerId(1L).build();
        Long offerId = 1L;
        // When
        bookMyTicketController.confirmSeats(bookingRequest, offerId);

        // Then
        verify(bookMyTicketService, times(1)).confirmSeats(bookingRequest, offerId);

    }

    @Test
    public void testOfferList() {
        // When
        bookMyTicketController.offerList();

        // Then
        verify(bookMyTicketService, times(1)).getOfferList();

    }
}
