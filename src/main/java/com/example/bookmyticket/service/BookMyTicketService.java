package com.example.bookmyticket.service;

import com.example.bookmyticket.dao.*;
import com.example.bookmyticket.dto.OfferDTO;
import com.example.bookmyticket.dto.ShowDTO;
import com.example.bookmyticket.dto.ShowSeatDTOResponse;
import com.example.bookmyticket.dto.TheaterDTO;
import com.example.bookmyticket.exception.CustomerNotFoundException;
import com.example.bookmyticket.exception.InvalidBookingException;
import com.example.bookmyticket.exception.PaymentFailedException;
import com.example.bookmyticket.exception.SeatUnavailableException;
import com.example.bookmyticket.repos.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookMyTicketService {

    public static final String BOOKING_CONFIRMED = "Booking Confirmed.";
    public static final String SEATS_UNAVAILABLE = "Seats Unavailable.";
    public static final String RESERVATION_SUCCESSFUL = "Reservation Successful.";
    public static final String INVALID_CUSTOMER_BOOKING = "Invalid Customer Booking.";
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
        try {
            log.info("Finding shows by theater name {} and city {}", theaterName, city);
            Optional<Theater> theater = theaterRepository.findByTheaterNameAndTheaterCity(theaterName, city);
            LocalDate currDate = LocalDate.now();
            LocalDate lastAvailableDate = currDate.plusDays(14);
            log.info("Is Theater present by given theater name and city? : {} ", theater.isPresent() ? "Yes" : "No");

            return showRepository.findAllShowsInRange(theater.get().getTheaterId(), currDate, lastAvailableDate)
                    .stream().map(show -> ShowDTO.builder()
                            .showId(show.getShowId())
                            .showDate(show.getShowDate())
                            .startTime(show.getStartTime())
                            .movieId(show.getMovieId())
                            .movieName(movieRepository.findById(show.getMovieId()).get().getMovieName())
                            .theaterId(show.getTheaterId())
                            .build()).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error in finding shows by theater name {} and city {} : ", theaterName, city, e);
            return new ArrayList<>();
        }
    }

    public List<TheaterDTO> getAllTheaters() {
        log.info("Finding all list of theaters");
        return theaterRepository.findAll().stream().map(theater -> TheaterDTO.builder()
                .theaterId(theater.getTheaterId())
                .theaterName(theater.getTheaterName())
                .theaterCity(theater.getTheaterCity())
                .build()).collect(Collectors.toList());
    }

    public ShowSeatDTOResponse findAllAvailableSeatsForShow(Long showId) {
        log.info("Finding all available seats for show id : {}", showId);
        List<ShowSeat> showSeats = showSeatRepository.findAllByShowIdAndStatus(showId, ShowSeat.BookingStatus.UNRESERVED);
        List<Long> availableshowSeatIdList = showSeats.stream().map(ShowSeat::getShowSeatId).collect(Collectors.toList());
        log.info("Number of available seats for show id {} : {}", showId, availableshowSeatIdList.size());
        return ShowSeatDTOResponse.builder()
                .showId(showSeats.get(0).getShowId())
                .availableShowSeatIDs(availableshowSeatIdList)
                .build();
    }

    @Transactional
    public String reserveSeats(BookingRequest bookingRequest) {

        try {
            log.info("Reserving seats for booking request : {}", bookingRequest);
            List<ShowSeat> showSeats = showSeatRepository.findAllByShowSeatIdIn(bookingRequest.getSeats());
            log.info("Validating reservation for show seats : {}", bookingRequest.getSeats());
            validateReservation(showSeats);
            if (bookingRequest.getSeats().size() != showSeats.size()) {
                throw new SeatUnavailableException("Seats are not available for reservation : " + bookingRequest.getSeats());
            }
            log.info("Validating valid customer for booking request : {}", bookingRequest.getCustomerId());
            Customer customer = customerRepository.findById(bookingRequest.getCustomerId())
                    .orElseThrow(CustomerNotFoundException::new);
            Booking booking = Booking.builder()
                    .customerId(customer.getCustomerId())
                    .totalAmount(getPaymentAmount(showSeats))
                    .showId(showSeats.get(0).getShowId())
                    .theaterId(theaterSeatRepository.findById(showSeats.get(0).getTheaterSeatId()).get().getTheaterId())
                    .reservationDate(LocalDateTime.now())
                    .isCancelled(false).build();
            bookingRepository.save(booking);
            log.info("Booking Saved : {}", booking);
            showSeats.forEach(showSeatItem -> {
                showSeatItem.setStatus(ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING);
                showSeatItem.setReservationTime(LocalDateTime.now());
                showSeatItem.setBookingId(booking.getBookingId());
            });
            showSeatRepository.saveAll(showSeats);
            log.info("Reservation successful for booking request : {}", bookingRequest);
            return RESERVATION_SUCCESSFUL;
        } catch (SeatUnavailableException | CannotAcquireLockException e) {
            log.error("Seats are not available for reservation : {}", bookingRequest.getSeats());
            return SEATS_UNAVAILABLE;
        } catch (CustomerNotFoundException e) {
            log.error("Customer not found for customer id : {}", bookingRequest.getCustomerId());
            return CUSTOMER_NOT_FOUND;
        } catch (Exception e) {
            log.error("Error in reserving seats for booking request : {} ", bookingRequest, e);
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
        if (CollectionUtils.isEmpty(showSeats) ||
                showSeats.stream().anyMatch(showSeat -> showSeat.getStatus() != ShowSeat.BookingStatus.UNRESERVED)) {
            throw new SeatUnavailableException();
        }
    }

    @Transactional
    public String confirmSeats(BookingRequest bookingRequest, Long offerId) {
        log.info("Confirming seats for booking request : {}", bookingRequest);
        List<ShowSeat> showSeats = showSeatRepository.findAllById(bookingRequest.getSeats());
        try {
            log.info("Validating seats for confirmation : {}", bookingRequest.getSeats());
            validateSeats(showSeats);
            log.info("Validating Customer booking for confirmation : {}", bookingRequest.getSeats());
            validateCustomerBooking(showSeats, bookingRequest);
            Booking booking = bookingRepository.findById(showSeats.get(0).getBookingId()).get();
            booking.setTotalAmount(getFinalPaymentAmount(showSeats, offerId));
            log.info("Final Payment Amount for bookingId {} is {}", booking.getBookingId(), booking.getTotalAmount());
            doPayment(booking, showSeats);
            log.info("Seats confirmed successfully for booking request : {}", bookingRequest.getSeats());
            return bookingConfirmedMessage(showSeats);
        } catch (SeatUnavailableException e) {
            log.error("Seats are not available for confirmation : {}", bookingRequest);
            return SEATS_UNAVAILABLE;
        } catch (InvalidBookingException e) {
            log.error("Invalid Customer Booking for booking request id : {}", showSeats.get(0).getBookingId());
            return INVALID_CUSTOMER_BOOKING;
        } catch (PaymentFailedException e) {
            log.error("Payment Failed for booking request id : {}", showSeats.get(0).getBookingId());
            return PAYMENT_FAILED;
        } catch (Exception e) {
            log.error("Error in confirming seats for booking request : {} ", bookingRequest, e);
            return e.getMessage();
        }
    }

    private String bookingConfirmedMessage(List<ShowSeat> showSeats) {
        return BOOKING_CONFIRMED + ".\n Your booking ID is : " +
                showSeats.get(0).getBookingId() + ".\n Your seats : " +
                showSeats.stream().map(ShowSeat::getShowSeatId).collect(Collectors.toList());
    }

    private void validateCustomerBooking(List<ShowSeat> showSeats, BookingRequest bookingRequest) throws InvalidBookingException {
        for (ShowSeat showSeat : showSeats) {
            if (!Objects.equals(bookingRepository.findById(showSeat.getBookingId()).get().getCustomerId(),
                    bookingRequest.getCustomerId())) {
                throw new InvalidBookingException();
            }
        }
    }

    private BigDecimal getFinalPaymentAmount(List<ShowSeat> showSeats, Long offerId) {

        BigDecimal total = new BigDecimal(0);
        for (ShowSeat showSeat : showSeats) {
            total = total.add(theaterSeatRepository.findById(showSeat.getTheaterSeatId()).get().getSeatPrice());
        }
        if (offerId != 0) {
            total = total.multiply(BigDecimal.valueOf(1L).subtract(offerRepository.findById(offerId).get().getOfferDiscount()));
        }
        return total;
    }

    @Transactional
    public void doPayment(Booking booking, List<ShowSeat> showSeats) throws PaymentFailedException {
        try {
            // Payment Gateway Integration
            log.info("Payment Done Successfully for booking : {}", booking);
            List<ShowSeat> checkedReservedSeatList = showSeatRepository.findAllByShowSeatIdInAndStatus(
                    showSeats.stream().map(ShowSeat::getShowSeatId).collect(Collectors.toList()),
                    ShowSeat.BookingStatus.RESERVED_PAYMENT_PENDING
            );
            if (checkedReservedSeatList.size() != showSeats.size()) {
                log.info("Reservation Expired, adding refund request for booking : {}", booking.getBookingId());
                refundRepository.save(Refund.builder()
                        .bookingId(booking.getBookingId())
                        .isRefunded(false)
                        .build());
                booking.setIsCancelled(true);
                bookingRepository.save(booking);
                log.info("Booking cancelled and refund request added for booking : {}", booking.getBookingId());
                throw new SeatUnavailableException(
                        "Your Reservation Expired. Please try again. Payment will be refunded in 2 business days"
                );
            }
            for (ShowSeat showSeat : showSeats) {
                showSeat.setStatus(ShowSeat.BookingStatus.CONFIRMED);
                showSeat.setBookingId(booking.getBookingId());
                showSeat.setReservationTime(LocalDateTime.now());
            }
            booking.setIsCancelled(false);
            bookingRepository.save(booking);
            showSeatRepository.saveAll(showSeats);
            log.info("Payment Done and seat confirmed successfully for booking : {}", booking);
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
        log.info("Finding all valid offers");
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
