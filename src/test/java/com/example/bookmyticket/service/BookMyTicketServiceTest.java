package com.example.bookmyticket.service;

import com.example.bookmyticket.repos.*;
import com.example.bookmyticket.dto.OfferDTO;
import com.example.bookmyticket.dto.ShowDTO;
import com.example.bookmyticket.dto.ShowSeatDTO;
import com.example.bookmyticket.dao.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BookMyTicketServiceTest {
    @Mock
    private TheaterRepository theaterRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private TheaterSeatRepository theaterSeatRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ShowSeatRepository showSeatRepository;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private BookingRepository bookingRepository;


    @InjectMocks
    private BookMyTicketService bookMyTicketService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllShowsByTheaterNameAndCity() {
        // Given
        String theaterName = "SampleTheater";
        String city = "SampleCity";

        List<Theater> theaterList = new ArrayList<>(); // Add sample theaters to the list
        Theater theaterNew = new Theater();
        theaterNew.setTheaterId(1L);
        theaterNew.setTheaterName("theaterName");
        theaterNew.setTheaterCity("city");
        theaterList.add(theaterNew);

        LocalDate currDate = LocalDate.now();
        LocalDate lastAvailableDate = currDate.minusDays(14);

        List<Show> sampleShows = new ArrayList<>();
        Show showNew = Show.builder()
                .showId(1L)
                .showDate(LocalDate.now())
                .theaterId(100L)
                .startTime("10:00")
                .runTime(165L)
                .movieId(1L)
                .build();
        sampleShows.add(showNew);// Add sample shows to the list

        // Mock behavior of theaterRepository
        when(theaterRepository.findAllByTheaterNameAndTheaterCity(theaterName, city)).thenReturn(theaterList);

        // Mock behavior of showRepository
        for (Theater theater : theaterList) {
            when(showRepository.findAllShowsInRange(theater.getTheaterId(), currDate, lastAvailableDate)).thenReturn(sampleShows);
        }

        // Mock behavior of movieRepository
        for (Show show : sampleShows) {
            when(movieRepository.findById(show.getMovieId())).thenReturn(Optional.of(new Movie())); // Replace 'new Movie()' with a sample Movie object
        }

        // When
        List<ShowDTO> result = bookMyTicketService.findAllShowsByTheaterNameAndCity(theaterName, city);

        // Then
        // Add assertions based on the expected result
        // For example, verify that the result contains the expected ShowDTO objects
    }

    @Test
    void testFindAllAvailableSeatsForShow() {
        // Arrange
        Long validShowId = 123L; // Replace with a valid show ID
        List<ShowSeat> availableShowSeats = new ArrayList<>();
        ShowSeat showSeat1 = ShowSeat.builder()
                .showSeatId(1L)
                .theaterSeatId(1L)
                .bookingId(null)
                .reservationTime(null)
                .showId(validShowId)
                .status(ShowSeat.BookingStatus.UNRESERVED)
                .build();
        availableShowSeats.add(showSeat1);// Create available ShowSeat objects
        when(showSeatRepository.findAllByShowIdAndStatus(validShowId, ShowSeat.BookingStatus.UNRESERVED))
                .thenReturn(availableShowSeats); // Mock showSeatRepository findAllByShowIdAndStatus

        // Act
        List<ShowSeatDTO> result = bookMyTicketService.findAllAvailableSeatsForShow(validShowId);

        // Assert
        assertEquals(availableShowSeats.size(), result.size());
    }

    @Test
    public void testReserveSeats() {
        // Create a BookingRequest object for testing
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setSeats(new ArrayList<>(List.of(1L, 2L, 3L)));
        bookingRequest.setCustomerId(1L);
        bookingRequest.setShowId(1L);

        // Mock the behavior of showSeatRepository.findAllByShowSeatIdIn method
        List<ShowSeat> mockShowSeats = new ArrayList<>();
        for (Long seatId : bookingRequest.getSeats()) {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setShowSeatId(seatId);
            showSeat.setShowId(1L);
            showSeat.setTheaterSeatId(1L);
            showSeat.setStatus(ShowSeat.BookingStatus.UNRESERVED);
            mockShowSeats.add(showSeat);
        }
        Booking booking = Booking.builder()
                .customerId(1L)
                .totalAmount(BigDecimal.valueOf(100))
                .showId(1L)
                .theaterId(1L)
                .reservationDate(LocalDateTime.now())
                .bookingId(1L)
                .build();
        // Add mock behavior for showSeatRepository.findAllByShowSeatIdIn to return the expected showSeats
        when(showSeatRepository.findAllByShowSeatIdIn(bookingRequest.getSeats())).thenReturn(mockShowSeats);

        // Mock the behavior of customerRepository.findById method
        Customer mockCustomer = new Customer(1L, "customerName");
        when(customerRepository.findById(bookingRequest.getCustomerId())).thenReturn(Optional.of(mockCustomer));

        TheaterSeat theaterSeat = TheaterSeat.builder()
                .theaterSeatId(1L)
                .seatPrice(BigDecimal.valueOf(100L))
                .seatType("Basic")
                .theaterId(100L)
                .build();
        // Mock the behavior of theaterSeatRepository.findById method
        when(theaterSeatRepository.findById(mockShowSeats.get(0).getTheaterSeatId())).thenReturn(Optional.of(theaterSeat));

        // Mock the behavior of bookingRepository.save method
        when(bookingRepository.save(booking)).thenReturn(new Booking());

        // Call the method under test
        String result = bookMyTicketService.reserveSeats(bookingRequest);

        // Verify the expected result
        assertEquals("Reservation Successful.", result);
    }

    @Test
    public void testReserveSeats_SeatUnavailable() {
        // Create a BookingRequest object for testing
        BookingRequest bookingRequest = new BookingRequest(/* Add necessary parameters */);

        // Mock the behavior of showSeatRepository.findAllByShowSeatIdIn method to return unavailable seats
        when(showSeatRepository.findAllByShowSeatIdIn(bookingRequest.getSeats())).thenReturn(new ArrayList<>());

        // Call the method under test
        String result = bookMyTicketService.reserveSeats(bookingRequest);

        // Verify the expected result
        assertEquals("Seats Unavailable.", result);
    }

    @Test
    public void testReserveSeats_CustomerNotFound() {
        // Create a BookingRequest object for testing
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setSeats(new ArrayList<>(List.of(1L, 2L, 3L)));
        bookingRequest.setCustomerId(1L);
        bookingRequest.setShowId(1L);

        // Mock the behavior of showSeatRepository.findAllByShowSeatIdIn method
        List<ShowSeat> mockShowSeats = new ArrayList<>();
        for (Long seatId : bookingRequest.getSeats()) {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setShowSeatId(seatId);
            showSeat.setShowId(1L);
            showSeat.setTheaterSeatId(1L);
            showSeat.setStatus(ShowSeat.BookingStatus.UNRESERVED);
            mockShowSeats.add(showSeat);
        }
        // Add mock behavior for showSeatRepository.findAllByShowSeatIdIn to return the expected showSeats
        when(showSeatRepository.findAllByShowSeatIdIn(bookingRequest.getSeats())).thenReturn(mockShowSeats);

        // Mock the behavior of customerRepository.findById method to return empty optional
        when(customerRepository.findById(bookingRequest.getCustomerId())).thenReturn(Optional.empty());

        // Call the method under test
        String result = bookMyTicketService.reserveSeats(bookingRequest);

        // Verify the expected result
        assertEquals("Customer Not Found.", result);
    }

    @Test
    public void testConfirmSeats_Successful() {
        // Create a BookingRequest object and offerId for testing
        BookingRequest bookingRequest = new BookingRequest(/* Add necessary parameters */);
        bookingRequest.setSeats(new ArrayList<>(List.of(1L, 2L, 3L)));
        bookingRequest.setCustomerId(1L);
        bookingRequest.setShowId(1L);
        Long offerId = 1L; // Replace with a valid offer ID

        // Mock the behavior of showSeatRepository.findAllById method
        List<ShowSeat> mockShowSeats = new ArrayList<>();
        for (Long seatId : bookingRequest.getSeats()) {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setShowSeatId(seatId);
            showSeat.setShowId(1L);
            showSeat.setTheaterSeatId(1L);
            showSeat.setStatus(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING);
            mockShowSeats.add(showSeat);
        }
        Booking booking = Booking.builder()
                .customerId(1L)
                .totalAmount(BigDecimal.valueOf(100))
                .showId(1L)
                .theaterId(1L)
                .reservationDate(LocalDateTime.now())
                .bookingId(1L)
                .build();
        TheaterSeat theaterSeat = TheaterSeat.builder()
                .theaterSeatId(1L)
                .seatPrice(BigDecimal.valueOf(100L))
                .seatType("Basic")
                .theaterId(100L)
                .build();
        // Mock the behavior of theaterSeatRepository.findById method
        when(theaterSeatRepository.findById(mockShowSeats.get(0).getTheaterSeatId())).thenReturn(Optional.of(theaterSeat));
        // Add mock behavior for showSeatRepository.findAllById to return the expected showSeats
        when(showSeatRepository.findAllById(bookingRequest.getSeats())).thenReturn(mockShowSeats);

        // Mock the behavior of bookingRepository.findById method
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));

        // Mock the behavior of offerRepository.findById method
        Offer mockOffer = Offer.builder()
                .offerName("offerName")
                .offerDiscount(BigDecimal.valueOf(0.2))
                .offerEndDate(LocalDate.now().plusDays(20))
                .offerStartDate(LocalDate.now())
                .offerId(1L).build();
        when(offerRepository.findById(offerId)).thenReturn(Optional.of(mockOffer));

        // Call the method under test
        String result = bookMyTicketService.confirmSeats(bookingRequest, offerId);

        // Verify the expected result
        assertEquals("Expected confirmation message", result);
    }

    @Test
    public void testConfirmSeats_SeatUnavailable() {
        // Create a BookingRequest object and offerId for testing
        BookingRequest bookingRequest = new BookingRequest(/* Add necessary parameters */);
        bookingRequest.setSeats(new ArrayList<>(List.of(1L, 2L, 3L)));
        bookingRequest.setCustomerId(1L);
        bookingRequest.setShowId(1L);
        Long offerId = 1L; // Replace with a valid offer ID

        // Mock the behavior of showSeatRepository.findAllById method
        List<ShowSeat> mockShowSeats = new ArrayList<>();
        for (Long seatId : bookingRequest.getSeats()) {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setShowSeatId(seatId);
            showSeat.setShowId(1L);
            showSeat.setTheaterSeatId(1L);
            showSeat.setStatus(ShowSeat.BookingStatus.UNRESERVED);
            mockShowSeats.add(showSeat);
        }


        // Mock the behavior of showSeatRepository.findAllById method to return unavailable seats
        when(showSeatRepository.findAllById(bookingRequest.getSeats())).thenReturn(mockShowSeats);

        // Call the method under test
        String result = bookMyTicketService.confirmSeats(bookingRequest, offerId);

        // Verify the expected result
        assertEquals("Seats Unavailable.", result);
    }

    @Test
    public void testConfirmSeats_InvalidBooking() {
        // Create a BookingRequest object and offerId for testing
        BookingRequest bookingRequest = new BookingRequest(/* Add necessary parameters */);
        bookingRequest.setSeats(new ArrayList<>(List.of(1L, 2L, 3L)));
        bookingRequest.setCustomerId(1L);
        bookingRequest.setShowId(1L);
        Long offerId = 1L; // Replace with a valid offer ID

        // Mock the behavior of showSeatRepository.findAllById method
        List<ShowSeat> mockShowSeats = new ArrayList<>();
        for (Long seatId : bookingRequest.getSeats()) {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setShowSeatId(seatId);
            showSeat.setShowId(1L);
            showSeat.setTheaterSeatId(1L);
            showSeat.setBookingId(2L);
            showSeat.setStatus(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING);
            mockShowSeats.add(showSeat);
        }
        Booking booking = Booking.builder()
                .customerId(null)
                .totalAmount(BigDecimal.valueOf(100))
                .showId(1L)
                .theaterId(1L)
                .reservationDate(LocalDateTime.now())
                .bookingId(2L)
                .build();

        // Mock the behavior to simulate an invalid booking scenario
        when(showSeatRepository.findAllById(bookingRequest.getSeats())).thenReturn(mockShowSeats);
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking)); // Mock a different customer ID

        // Call the method under test
        String result = bookMyTicketService.confirmSeats(bookingRequest, offerId);

        // Verify the expected result
        assertEquals("Invalid Booking.", result);
    }

    @Test
public void testGetOfferList() {
        // Arrange
        List<OfferDTO> validOffersDTO = new ArrayList<>();
        OfferDTO offerDTO = OfferDTO.builder()
                .offerId(1L)
                .offerName("OfferName")
                .offerDiscount(BigDecimal.valueOf(10))
                .offerStartDate(LocalDate.now())
                .offerEndDate(LocalDate.now().plusDays(10))
                .build();
        validOffersDTO.add(offerDTO);

        List<Offer> validOffers = new ArrayList<>();
        Offer offer = Offer.builder()
                .offerId(1L)
                .offerName("OfferName")
                .offerDiscount(BigDecimal.valueOf(10))
                .offerStartDate(LocalDate.now())
                .offerEndDate(LocalDate.now().plusDays(10))
                .build();
        validOffers.add(offer);// Create valid Offer objects
        when(offerRepository.findAllValidOffers(LocalDate.now())).thenReturn(validOffers); // Mock offerRepository findAll

        // Act
        List<OfferDTO> result = bookMyTicketService.getOfferList();

        // Assert
        assertEquals(validOffersDTO, result);
    }
}
