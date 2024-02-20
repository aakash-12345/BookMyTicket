package com.example.bookmyticket.service;

import com.example.bookmyticket.data.*;
import com.example.bookmyticket.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.util.List;

@Service
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
    public void addTheaters(List<Theater> theaters) {
        theaterRepository.saveAll(theaters);
    }

    @Transactional
    public void addMovies(List<Movie> movies) {
        movieRepository.saveAll(movies);
    }

    @Transactional
    public void addShows(List<Show> shows) {
        for (Show show : shows) {
            List<TheaterSeat> theaterSeats = theaterSeatRepository.findByTheaterId(show.getTheaterId());
            for (TheaterSeat theaterSeat : theaterSeats) {
                ShowSeat showSeat = new ShowSeat();
                showSeat.setShowId(show.getShowId());
                showSeat.setTheaterSeatId(theaterSeat.getTheaterSeatId());
                showSeatRepository.save(showSeat);
            }
            showRepository.save(show);
        }

    }

    @Transactional
    public void addTheaterSeats(List<TheaterSeat> theaterSeats) {
        theaterSeatRepository.saveAll(theaterSeats);
    }

    @Transactional
    public void addCustomers(List<Customer> customers) {
        customerRepository.saveAll(customers);
    }

    @Transactional
    public void addOffers(List<Offer> offers) {
        offerRepository.saveAll(offers);
    }
}
