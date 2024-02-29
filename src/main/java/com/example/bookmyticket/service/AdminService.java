package com.example.bookmyticket.service;

import com.example.bookmyticket.dao.*;
import com.example.bookmyticket.dto.*;
import com.example.bookmyticket.repos.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class AdminService {


    private final TheaterRepository theaterRepository;

    private final MovieRepository movieRepository;

    private final ShowRepository showRepository;

    private final TheaterSeatRepository theaterSeatRepository;

    private final CustomerRepository customerRepository;

    private final ShowSeatRepository showSeatRepository;

    private final OfferRepository offerRepository;

    @Transactional
    public void addTheaters(List<TheaterDTO> theaters) {
        log.info("Adding theaters");
        List<Theater> theaterList = theaters.stream().map(theaterDTO -> Theater.builder()
                .theaterId(theaterDTO.getTheaterId())
                .theaterName(theaterDTO.getTheaterName())
                .theaterCity(theaterDTO.getTheaterCity()).build()).collect(Collectors.toList());
        theaterRepository.saveAll(theaterList);
        log.info("Theaters added successfully");
    }

    @Transactional
    public void addMovies(List<MovieDTO> movies) {
        log.info("Adding movies");
        List<Movie> movieList = movies.stream().map(movieDTO -> Movie.builder()
                .movieId(movieDTO.getMovieId())
                .runTime(movieDTO.getRunTime())
                .movieName(movieDTO.getMovieName()).build()).collect(Collectors.toList());
        movieRepository.saveAll(movieList);
        log.info("Movies added successfully");
    }

    @Transactional
    public void addShows(List<ShowDTO> shows) {
        log.info("Adding Shows");
        List<Show> showList = shows.stream().map(show -> Show.builder()
                .showId(show.getShowId())
                .showDate(show.getShowDate())
                .startTime(show.getStartTime())
                .movieId(show.getMovieId())
                .theaterId(show.getTheaterId())
                .build()).collect(Collectors.toList());
        for (Show show : showList) {
            List<TheaterSeat> theaterSeats = theaterSeatRepository.findByTheaterId(show.getTheaterId());
            for (TheaterSeat theaterSeat : theaterSeats) {
                ShowSeat showSeat = new ShowSeat();
                showSeat.setShowId(show.getShowId());
                showSeat.setTheaterSeatId(theaterSeat.getTheaterSeatId());
                showSeatRepository.save(showSeat);
            }
            showRepository.save(show);
        }
        log.info("Shows added successfully");

    }

    @Transactional
    public void addTheaterSeats(List<TheaterSeatDTO> theaterSeats) {
        log.info("Adding theater seats");
        List<TheaterSeat> theaterSeatList = theaterSeats.stream().map(theaterSeatDTO -> TheaterSeat.builder()
                .theaterSeatId(theaterSeatDTO.getTheaterSeatId())
                .seatType(theaterSeatDTO.getSeatType())
                .seatPrice(theaterSeatDTO.getSeatPrice())
                .theaterId(theaterSeatDTO.getTheaterId())
                .build()).collect(Collectors.toList());
        theaterSeatRepository.saveAll(theaterSeatList);
        log.info("Theater seats added successfully");
    }

    @Transactional
    public void addCustomers(List<CustomerDTO> customers) {
        log.info("Adding customers");
        List<Customer> customerList = customers.stream().map(customerDTO -> Customer.builder()
                .customerId(customerDTO.getCustomerId())
                .customerName(customerDTO.getCustomerName()).build()).collect(Collectors.toList());
        customerRepository.saveAll(customerList);
        log.info("Customers added successfully");
    }

    @Transactional
    public void addOffers(List<OfferDTO> offers) {
        log.info("Adding offers");
        List<Offer> offerList = offers.stream().map(offerDTO -> Offer.builder()
                .offerId(offerDTO.getOfferId())
                .offerStartDate(offerDTO.getOfferStartDate())
                .offerDiscount(offerDTO.getOfferDiscount())
                .offerName(offerDTO.getOfferName())
                .offerEndDate(offerDTO.getOfferEndDate()).build()).collect(Collectors.toList());
        offerRepository.saveAll(offerList);
        log.info("Offers added successfully");
    }
}
