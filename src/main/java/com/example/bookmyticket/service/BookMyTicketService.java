package com.example.bookmyticket.service;

import com.example.bookmyticket.data.*;
import com.example.bookmyticket.dto.ShowDTO;
import com.example.bookmyticket.dto.ShowSeatDTO;
import com.example.bookmyticket.exception.CustomerNotFoundException;
import com.example.bookmyticket.exception.InvalidBookingException;
import com.example.bookmyticket.exception.SeatUnavailableException;
import com.example.bookmyticket.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookMyTicketService {

    public static final String BOOKING_CONFIRMED = "Booking Confirmed.";
    public static final String SEATS_UNAVAILABLE = "Seats Unavailable.";
    public static final String RESERVATION_SUCCESSFUL = "Reservation Successful.";
    public static final String INVALID_BOOKING = "Invalid Booking.";
    public static final String CUSTOMER_NOT_FOUND = "Customer Not Found";

    private final TheaterRepository theaterRepository;

    private final MovieRepository movieRepository;

    private final ShowRepository showRepository;

    private final TheaterSeatRepository theaterSeatRepository;

    private final CustomerRepository customerRepository;

    private final ShowSeatRepository showSeatRepository;

    private final BookingRepository bookingRepository;

    public List<ShowDTO> findAllShowsByTheaterNameAndCity(String theaterName, String city) {
        List<Theater> theaterList = theaterRepository.findAllByTheaterNameAndTheaterCity(theaterName, city);
        LocalDate currDate = LocalDate.now();
        LocalDate lastAvailableDate = currDate.minusDays(14);
        List<Show> shows = new ArrayList<>();
        theaterList.forEach(theater -> {
            shows.addAll(showRepository.findAllShowsInRange(theater.getTheaterId(), currDate, lastAvailableDate));
        });

        return shows.stream().map(show -> ShowDTO.builder()
                .showId(show.getShowId())
                .showDate(show.getShowDate())
                .startTime(show.getStartTime())
                .movieId(show.getMovieId())
                .movieName(movieRepository.findById(show.getMovieId()).get().getMovieName())
                .runTime(show.getRunTime())
                .theaterId(show.getTheaterId())
                .build()).collect(Collectors.toList());
    }

    public List<ShowSeatDTO> findAllAvailableSeatsForShow(Long showId) {
        List<ShowSeat> showSeats = showSeatRepository.findAllByShowIdAndStatus(showId, ShowSeat.BookingStatus.UNRESERVED);
        return showSeats.stream().map(showSeat -> ShowSeatDTO.builder()
                .showId(showSeat.getShowId())
                .showSeatId(showSeat.getShowSeatId())
                .build()).collect(Collectors.toList());
    }

    @Transactional
    // Need transaction as we are updating many tables : BOOKING, SHOW_SEAT
    public String reserveSeats(BookingRequest bookingRequest) {
        List<ShowSeat> showSeats = showSeatRepository.findAllByShowSeatIdIn(bookingRequest.getSeats());
        //This locks all showseats to be booked for customer
        try {
            validateReservation(showSeats);
            //all seats available for reservation
            Customer customer = customerRepository.findById(bookingRequest.getCustomerId()).orElseThrow(CustomerNotFoundException::new);
            final Booking booking = new Booking();
            booking.setCustomerId(customer.getCustomerId());
            booking.setTotalAmount(getPaymentAmount(showSeats));
            booking.setShowId(showSeats.get(0).getShowId());
            booking.setTheaterId(theaterSeatRepository.findById(showSeats.get(0).getTheaterSeatId()).get().getTheaterId());
            booking.setReservationDate(LocalDateTime.now());
            bookingRepository.save(booking);
            for (ShowSeat showSeat : showSeats) {
                showSeat.setStatus(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING);
                showSeat.setReservationTime(LocalDateTime.now());
            }
            return RESERVATION_SUCCESSFUL;
        } catch (SeatUnavailableException e) {
            return SEATS_UNAVAILABLE;
        } catch (CustomerNotFoundException e) {
            return CUSTOMER_NOT_FOUND;
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    private BigDecimal getPaymentAmount(List<ShowSeat> showSeats) {
        BigDecimal total = new BigDecimal(0);
        for (ShowSeat showSeat : showSeats) {
            total = total.add(theaterSeatRepository.findById(showSeat.getTheaterSeatId()).get().getSeatPrice());
        }
        return total;
    }

    private void validateReservation(List<ShowSeat> showSeats) throws SeatUnavailableException {
        if (CollectionUtils.isEmpty(showSeats) || showSeats.stream().anyMatch(showSeat -> showSeat.getStatus() != ShowSeat.BookingStatus.UNRESERVED)) {
            throw new SeatUnavailableException();
        }
    }

    @Transactional
    //called once payment is done or session times out
    public String confirmSeats(BookingRequest bookingRequest) {
        List<ShowSeat> showSeats = showSeatRepository.findAllById(bookingRequest.getSeats());
        try {
            validateSeats(showSeats);
            validateBooking(showSeats, bookingRequest);
            for (ShowSeat showSeat : showSeats) {
                showSeat.setStatus(ShowSeat.BookingStatus.CONFIRMED);
            }
            return bookingConfirmedMessage(showSeats);
        } catch (SeatUnavailableException e) {
            return SEATS_UNAVAILABLE;
        } catch (InvalidBookingException e) {
            return INVALID_BOOKING;
        }
    }

    private String bookingConfirmedMessage(List<ShowSeat> showSeats) {
        return BOOKING_CONFIRMED + ". Your booking ID is : " + showSeats.get(0).getBooking().getId() + ". Your seats : " + showSeats.stream().map(showSeat -> showSeat.getTheaterSeat().getId()).collect(Collectors.toList());
    }

    private void validateBooking(List<ShowSeat> showSeats, BookingRequest bookingRequest) throws InvalidBookingException {
        for (ShowSeat showSeat : showSeats) {
            if (showSeat.getBooking().getBookedBy().getId() != bookingRequest.getCustomerId()) {
                throw new InvalidBookingException();
            }
        }
    }

    private void validateSeats(List<ShowSeat> showSeats) throws SeatUnavailableException {
        for (ShowSeat showSeat : showSeats) {
            if (ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING != showSeat.getStatus()) {
                throw new SeatUnavailableException();
            }
        }
    }

}
