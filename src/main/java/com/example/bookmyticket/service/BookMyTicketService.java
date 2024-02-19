package com.example.bookmyticket.service;

import com.example.bookmyticket.data.*;
import com.example.bookmyticket.dto.ShowDTO;
import com.example.bookmyticket.dto.ShowSeatDTO;
import com.example.bookmyticket.exception.CustomerNotFoundException;
import com.example.bookmyticket.exception.InvalidBookingException;
import com.example.bookmyticket.exception.SeatUnavailableException;
import com.example.bookmyticket.model.*;
import com.example.bookmyticket.offers.OfferProcessorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookMyTicketService {

    public static final String BOOKING_CONFIRMED = "Booking Confirmed.";
    public static final String SEATS_UNAVAILABLE = "Seats Unavailable.";
    public static final String RESERVATION_SUCCESSFUL = "Reservation Successful.";
    public static final String INVALID_BOOKING = "Invalid Booking.";
    public static final int SEAT_COST = 100;
    public static final String CUSTOMER_NOT_FOUND = "Customer Not Found";

    private final TheaterRepository theaterRepository;

    private final MovieRepository movieRepository;

    private final ShowRepository showRepository;

    private final TheaterSeatRepository theaterSeatRepository;

    private final CustomerRepository customerRepository;

    private final ShowSeatRepository showSeatRepository;

    private final BookingRepository bookingRepository;

    private final OfferProcessorFactory offerProcessorFactory;

    @Value("${theater.seat.basic.price}")
    private BigDecimal basicPrice;

    @Value("${theater.seat.premium.price}")
    private BigDecimal premiumPrice;

    @Transactional
    public List<ShowDTO> findAllShowsByMovieAndDateAndCity(String moviename, LocalDate date, String city) {
        List<Show> shows = showRepository.findAllShowsNative(moviename, date, city);
        return shows.stream().map(show -> ShowDTO.builder()
                .id(show.getId())
                .date(show.getDate())
                .startTime(show.getStartTime())
                .movie(show.getMovie()).build()).collect(Collectors.toList());
    }

    public List<ShowSeatDTO> findAllAvailableSeatsForShow(Long showid) {
        List<ShowSeat> showSeats = showSeatRepository.findAllNonPendingNonConfirmedShowSeatsNative(showid, ShowSeat.BookingStatus.CONFIRMED.toString(), ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING.toString());
        return showSeats.stream().map(showSeat -> ShowSeatDTO.builder()
                .booking(showSeat.getBooking())
                .reservationTime(showSeat.getReservationTime())
                .status(showSeat.getStatus())
                .showid(showSeat.getShow().getId())
                .id(showSeat.getId())
                .build()).collect(Collectors.toList());
    }

    @Transactional
    // Need transaction as we are updating many tables : BOOKING, SHOW_SEAT
    public String reserveSeats(BookingRequest bookingRequest) {
        List<ShowSeat> showSeats = showSeatRepository.findAllById(bookingRequest.getSeats());
        //This locks all showseats to be booked for customer
        try {
            validateReservation(showSeats);
            //all seats available for reservation
            Customer customer = customerRepository.findById(bookingRequest.getCustomerId()).orElseThrow(CustomerNotFoundException::new);
            final Booking booking = new Booking();
            booking.setBookedBy(customer);
            booking.setTotalAmount(getPaymentAmount(showSeats, bookingRequest));
            Theater theater = showSeats.get(0).getTheaterSeat().getTheater();
            List<Offer> offers = theater.getOffers();
            offers.forEach(offer -> {

                offerProcessorFactory.getOfferProcessor(offer.getOfferType()).process(showSeats, booking);
            });
            Booking finalBooking = bookingRepository.save(booking);
            for (ShowSeat showSeat : showSeats) {
                showSeat.setBooking(finalBooking);
                showSeat.setStatus(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING);
                showSeat.setReservationTime(LocalDateTime.now());
            }
            return RESERVATION_SUCCESSFUL;
        } catch (SeatUnavailableException e) {
            return SEATS_UNAVAILABLE;
        } catch (CustomerNotFoundException e) {
            return CUSTOMER_NOT_FOUND;
        }
    }

    private BigDecimal getPaymentAmount(List<ShowSeat> showSeats, BookingRequest bookingRequest) {
        BigDecimal total = new BigDecimal(0);
        for (ShowSeat showSeat : showSeats) {
            total = total.add(showSeat.getTheaterSeat().getSeatPrice());
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
