package com.example.bookmyticket.service;

import com.example.bookmyticket.dto.*;
import com.example.bookmyticket.repos.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookMyTicketServiceTest {
    @Mock
    private TheaterRepository theaterRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private RefundRepository refundRepository;

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
        String theaterName = "SampleTheater";
        String city = "SampleCity";

        Theater theaterNew = new Theater();
        theaterNew.setTheaterId(1L);
        theaterNew.setTheaterName("theaterName");
        theaterNew.setTheaterCity("city");

        LocalDate currDate = LocalDate.now();
        LocalDate availableDate = currDate.plusDays(14);

        List<Show> sampleShows = new ArrayList<>();
        Show showNew = Show.builder()
                .showId(1L)
                .showDate(LocalDate.now())
                .theaterId(100L)
                .startTime("10:00")
                .movieId(1L)
                .build();
        sampleShows.add(showNew);
        when(theaterRepository.findByTheaterNameAndTheaterCity(theaterName, city)).thenReturn(Optional.of(theaterNew));


        when(showRepository.findAllShowsInRange(theaterNew.getTheaterId(), currDate, availableDate)).thenReturn(sampleShows);


        for (Show show : sampleShows) {
            when(movieRepository.findById(show.getMovieId())).thenReturn(Optional.of(new Movie(1L, "movie1", 165L)));
        }

        List<ShowDTO> result = bookMyTicketService.findAllShowsByTheaterNameAndCity(theaterName, city);

        assertEquals(sampleShows.size(), result.size());

    }

    @Test
    void getAllTheaters() {
        List<Theater> theaters = new ArrayList<>();
        Theater theater = Theater.builder()
                .theaterId(1L)
                .theaterName("theaterName")
                .theaterCity("city").build();
        theaters.add(theater);

        List<TheaterDTO> theaterDTOs = new ArrayList<>();
        TheaterDTO theaterDTO = TheaterDTO.builder()
                .theaterId(1L)
                .theaterName("theaterName")
                .theaterCity("city").build();
        theaterDTOs.add(theaterDTO);
        when(theaterRepository.findAll()).thenReturn(theaters);
        List<TheaterDTO> result = bookMyTicketService.getAllTheaters();
        assertEquals(theaterDTOs, result);
    }

    @Test
    void testFindAllAvailableSeatsForShow() {
        Long validShowId = 123L;
        List<ShowSeat> availableShowSeats = new ArrayList<>();
        ShowSeat showSeat1 = ShowSeat.builder()
                .showSeatId(1L)
                .theaterSeatId(1L)
                .bookingId(null)
                .reservationTime(null)
                .showId(validShowId)
                .status(ShowSeat.BookingStatus.UNRESERVED)
                .build();
        availableShowSeats.add(showSeat1);
        when(showSeatRepository.findAllByShowIdAndStatus(validShowId, ShowSeat.BookingStatus.UNRESERVED))
                .thenReturn(availableShowSeats);

        List<ShowSeatDTOResponse> result = bookMyTicketService.findAllAvailableSeatsForShow(validShowId);

        assertEquals(availableShowSeats.size(), result.size());
    }

    @Test
    public void testReserveSeats() {

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setSeats(new ArrayList<>(List.of(1L, 2L, 3L)));
        bookingRequest.setCustomerId(1L);
        bookingRequest.setShowId(1L);

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

        when(showSeatRepository.findAllByShowSeatIdIn(bookingRequest.getSeats())).thenReturn(mockShowSeats);

        Customer mockCustomer = new Customer(1L, "customerName");
        when(customerRepository.findById(bookingRequest.getCustomerId())).thenReturn(Optional.of(mockCustomer));

        TheaterSeat theaterSeat = TheaterSeat.builder()
                .theaterSeatId(1L)
                .seatPrice(BigDecimal.valueOf(100L))
                .seatType("Basic")
                .theaterId(100L)
                .build();

        when(theaterSeatRepository.findById(mockShowSeats.get(0).getTheaterSeatId())).thenReturn(Optional.of(theaterSeat));

        when(bookingRepository.save(booking)).thenReturn(new Booking());

        String result = bookMyTicketService.reserveSeats(bookingRequest);

        assertEquals("Reservation Successful.", result);
    }

    @Test
    public void testReserveSeats_SeatUnavailable() {

        BookingRequest bookingRequest = new BookingRequest();

        when(showSeatRepository.findAllByShowSeatIdIn(bookingRequest.getSeats())).thenReturn(new ArrayList<>());

        String result = bookMyTicketService.reserveSeats(bookingRequest);

        assertEquals("Seats Unavailable.", result);
    }

    @Test
    public void testReserveSeats_CustomerNotFound() {

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setSeats(new ArrayList<>(List.of(1L, 2L, 3L)));
        bookingRequest.setCustomerId(1L);
        bookingRequest.setShowId(1L);

        List<ShowSeat> mockShowSeats = new ArrayList<>();
        for (Long seatId : bookingRequest.getSeats()) {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setShowSeatId(seatId);
            showSeat.setShowId(1L);
            showSeat.setTheaterSeatId(1L);
            showSeat.setStatus(ShowSeat.BookingStatus.UNRESERVED);
            mockShowSeats.add(showSeat);
        }

        when(showSeatRepository.findAllByShowSeatIdIn(bookingRequest.getSeats())).thenReturn(mockShowSeats);

        when(customerRepository.findById(bookingRequest.getCustomerId())).thenReturn(Optional.empty());

        String result = bookMyTicketService.reserveSeats(bookingRequest);

        assertEquals("Customer Not Found.", result);
    }

    @Test
    public void testConfirmSeats_Successful() {

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setSeats(new ArrayList<>(List.of(1L, 2L, 3L)));
        bookingRequest.setCustomerId(1L);
        bookingRequest.setShowId(1L);
        Long offerId = 1L;

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

        when(theaterSeatRepository.findById(mockShowSeats.get(0).getTheaterSeatId())).thenReturn(Optional.of(theaterSeat));

        when(showSeatRepository.findAllById(bookingRequest.getSeats())).thenReturn(mockShowSeats);

        when(showSeatRepository.findAllByShowSeatIdInAndStatus(bookingRequest.getSeats(), ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING)).thenReturn(mockShowSeats);

        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));

        Offer mockOffer = Offer.builder()
                .offerName("offerName")
                .offerDiscount(BigDecimal.valueOf(0.2))
                .offerEndDate(LocalDate.now().plusDays(20))
                .offerStartDate(LocalDate.now())
                .offerId(1L).build();
        when(offerRepository.findById(offerId)).thenReturn(Optional.of(mockOffer));

        String result = bookMyTicketService.confirmSeats(bookingRequest, offerId);

        assertTrue(result.startsWith(BookMyTicketService.BOOKING_CONFIRMED));
    }

    @Test
    public void testConfirmSeats_Failed_In_Concurrency() {

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setSeats(new ArrayList<>(List.of(1L, 2L, 3L)));
        bookingRequest.setCustomerId(1L);
        bookingRequest.setShowId(1L);
        Long offerId = 1L;

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

        when(theaterSeatRepository.findById(mockShowSeats.get(0).getTheaterSeatId())).thenReturn(Optional.of(theaterSeat));

        when(showSeatRepository.findAllById(bookingRequest.getSeats())).thenReturn(mockShowSeats);

        when(showSeatRepository.findAllByShowSeatIdInAndStatus(bookingRequest.getSeats(), ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING)).thenReturn(new ArrayList<>());

        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));

        Offer mockOffer = Offer.builder()
                .offerName("offerName")
                .offerDiscount(BigDecimal.valueOf(0.2))
                .offerEndDate(LocalDate.now().plusDays(20))
                .offerStartDate(LocalDate.now())
                .offerId(1L).build();
        when(offerRepository.findById(offerId)).thenReturn(Optional.of(mockOffer));

        String result = bookMyTicketService.confirmSeats(bookingRequest, offerId);

        assertFalse(result.startsWith(BookMyTicketService.BOOKING_CONFIRMED));
    }

    @Test
    public void testConfirmSeats_SeatUnavailable() {

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setSeats(new ArrayList<>(List.of(1L, 2L, 3L)));
        bookingRequest.setCustomerId(1L);
        bookingRequest.setShowId(1L);
        Long offerId = 1L;

        List<ShowSeat> mockShowSeats = new ArrayList<>();
        for (Long seatId : bookingRequest.getSeats()) {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setShowSeatId(seatId);
            showSeat.setShowId(1L);
            showSeat.setTheaterSeatId(1L);
            showSeat.setStatus(ShowSeat.BookingStatus.UNRESERVED);
            mockShowSeats.add(showSeat);
        }

        when(showSeatRepository.findAllById(bookingRequest.getSeats())).thenReturn(mockShowSeats);

        String result = bookMyTicketService.confirmSeats(bookingRequest, offerId);

        assertEquals("Seats Unavailable.", result);
    }

    @Test
    public void testConfirmSeats_InvalidBooking() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setSeats(new ArrayList<>(List.of(1L, 2L, 3L)));
        bookingRequest.setCustomerId(1L);
        bookingRequest.setShowId(1L);
        Long offerId = 1L;

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

        when(showSeatRepository.findAllById(bookingRequest.getSeats())).thenReturn(mockShowSeats);
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));

        String result = bookMyTicketService.confirmSeats(bookingRequest, offerId);

        assertEquals("Invalid Customer Booking.", result);
    }

    @Test
    public void testGetOfferList() {

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
        validOffers.add(offer);
        when(offerRepository.findAllValidOffers(LocalDate.now())).thenReturn(validOffers);

        List<OfferDTO> result = bookMyTicketService.getOfferList();

        assertEquals(validOffersDTO, result);
    }
}
