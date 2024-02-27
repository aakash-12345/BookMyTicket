package com.example.bookmyticket.service;

import com.example.bookmyticket.repos.*;
import com.example.bookmyticket.dto.OfferDTO;
import com.example.bookmyticket.dto.ShowDTO;
import com.example.bookmyticket.dto.ShowSeatDTO;
import com.example.bookmyticket.exception.CustomerNotFoundException;
import com.example.bookmyticket.exception.InvalidBookingException;
import com.example.bookmyticket.exception.PaymentFailedException;
import com.example.bookmyticket.exception.SeatUnavailableException;
import com.example.bookmyticket.dao.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookMyTicketService {

    public static final String BOOKING_CONFIRMED = "Booking Confirmed.";
    public static final String SEATS_UNAVAILABLE = "Seats Unavailable.";
    public static final String RESERVATION_SUCCESSFUL = "Reservation Successful.";
    public static final String INVALID_BOOKING = "Invalid Booking.";
    public static final String CUSTOMER_NOT_FOUND = "Customer Not Found.";
    public static final String PAYMENT_FAILED = "Payment is Failed.";

    private final TheaterRepository theaterRepository;

    private final MovieRepository movieRepository;

    private final ShowRepository showRepository;

    private final TheaterSeatRepository theaterSeatRepository;

    private final CustomerRepository customerRepository;

    private final ShowSeatRepository showSeatRepository;

    private final BookingRepository bookingRepository;

    private final OfferRepository offerRepository;

    private final RefundRepository refundRepository;

    public List<ShowDTO> findAllShowsByTheaterNameAndCity(String theaterName, String city) {
        List<Theater> theaterList = theaterRepository.findAllByTheaterNameAndTheaterCity(theaterName, city);
        LocalDate currDate = LocalDate.now();
        LocalDate lastAvailableDate = currDate.minusDays(14);
        List<Show> shows = new ArrayList<>();
        theaterList.forEach(theater -> {
            final var listOfShows = showRepository.findAllShowsInRange(theater.getTheaterId(), currDate, lastAvailableDate);
            shows.addAll(listOfShows);
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
    public String reserveSeats(BookingRequest bookingRequest) {
        List<ShowSeat> showSeats = showSeatRepository.findAllByShowSeatIdIn(bookingRequest.getSeats());
        try {
            validateReservation(showSeats);
            Customer customer = customerRepository.findById(bookingRequest.getCustomerId()).orElseThrow(CustomerNotFoundException::new);
            Booking booking = Booking.builder()
                    .customerId(customer.getCustomerId())
                    .totalAmount(getPaymentAmount(showSeats))
                    .showId(showSeats.get(0).getShowId())
                    .theaterId(theaterSeatRepository.findById(showSeats.get(0).getTheaterSeatId()).get().getTheaterId())
                    .reservationDate(LocalDateTime.now())
                    .isCancelled(false).build();
            bookingRepository.save(booking);
            for (ShowSeat showSeat : showSeats) {
                showSeat.setStatus(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING);
                showSeat.setReservationTime(LocalDateTime.now());
                showSeat.setBookingId(booking.getBookingId());
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

    public String confirmSeats(BookingRequest bookingRequest, Long offerId) {
        List<ShowSeat> showSeats = showSeatRepository.findAllById(bookingRequest.getSeats());
        try {
            validateSeats(showSeats);
            validateBooking(showSeats, bookingRequest);
            Booking booking = bookingRepository.findById(showSeats.get(0).getBookingId()).get();
            booking.setTotalAmount(getFinalPaymentAmount(showSeats, offerId));
            doPayment(booking, showSeats);
            return bookingConfirmedMessage(showSeats);
        } catch (SeatUnavailableException e) {
            return SEATS_UNAVAILABLE;
        } catch (InvalidBookingException e) {
            return INVALID_BOOKING;
        } catch (PaymentFailedException e) {
            return PAYMENT_FAILED;
        }
    }

    private String bookingConfirmedMessage(List<ShowSeat> showSeats) {
        return BOOKING_CONFIRMED + ".\n Your booking ID is : " + showSeats.get(0).getBookingId() + ".\n Your seats : " + showSeats.stream().map(ShowSeat::getShowSeatId).collect(Collectors.toList());
    }

    private void validateBooking(List<ShowSeat> showSeats, BookingRequest bookingRequest) throws InvalidBookingException {
        for (ShowSeat showSeat : showSeats) {
            if (!Objects.equals(bookingRepository.findById(showSeat.getBookingId()).get().getCustomerId(), bookingRequest.getCustomerId())) {
                throw new InvalidBookingException();
            }
        }
    }

    private BigDecimal getFinalPaymentAmount(List<ShowSeat> showSeats, Long offerId) {

        BigDecimal total = new BigDecimal(0);
        for (ShowSeat showSeat : showSeats) {
            total = total.add(theaterSeatRepository.findById(showSeat.getTheaterSeatId()).get().getSeatPrice());
        }
        total = total.multiply(BigDecimal.valueOf(1L).subtract(offerRepository.findById(offerId).get().getOfferDiscount()));
        return total;
    }

    @Transactional
    public void doPayment(Booking booking, List<ShowSeat> showSeats) throws PaymentFailedException {
        try {
            // Payment Gateway Integration
            List<ShowSeat> checkeReservedSeatList = showSeatRepository.findAllByShowSeatIdInAndStatus(showSeats.stream().map(ShowSeat::getShowSeatId).collect(Collectors.toList()), ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING);
            if (checkeReservedSeatList.size() != showSeats.size()) {
                refundRepository.save(Refund.builder()
                        .bookingId(booking.getBookingId())
                        .build());
                booking.setIsCancelled(true);
                bookingRepository.save(booking);
                throw new SeatUnavailableException("Seat is already confirmed. Please try again. Payment will be refunded in 2 business days");
            }
            for (ShowSeat showSeat : showSeats) {
                    showSeat.setStatus(ShowSeat.BookingStatus.CONFIRMED);
                    showSeat.setBookingId(booking.getBookingId());
                    showSeat.setReservationTime(LocalDateTime.now());
            }
            booking.setIsCancelled(false);
            bookingRepository.save(booking);
            showSeatRepository.saveAll(showSeats);
        } catch (Exception e) {
            throw new PaymentFailedException(e.getMessage());
        }
    }

    private void validateSeats(List<ShowSeat> showSeats) throws SeatUnavailableException {
        for (ShowSeat showSeat : showSeats) {
            if (ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING != showSeat.getStatus()) {
                throw new SeatUnavailableException();
            }
        }
    }

    public List<OfferDTO> getOfferList() {
        List<Offer> offerList = offerRepository.findAllValidOffers(LocalDate.now());
        return offerList.stream().map(offer -> OfferDTO.builder()
                .offerId(offer.getOfferId())
                .offerName(offer.getOfferName())
                .offerDiscount(offer.getOfferDiscount())
                .offerStartDate(offer.getOfferStartDate())
                .offerEndDate(offer.getOfferEndDate())
                .build()).collect(Collectors.toList());
    }

}
