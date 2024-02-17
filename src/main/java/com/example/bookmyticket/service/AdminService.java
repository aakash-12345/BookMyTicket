package com.example.bookmyticket.service;

import com.example.bookmyticket.data.*;
import com.example.bookmyticket.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @PersistenceContext
    EntityManager em;

    public void addTheaters(List<Theater> theaters) {
        for (Theater theater : theaters) {
            theaterRepository.save(theater);
        }
    }

    public void addMovies(List<Movie> movies) {
        for (Movie movie : movies) {
            movieRepository.save(movie);
        }
    }

    @Transactional
    //We need transactions because we are adding rows to 2 tables SHOW & SHOW_SEAT
    public void addShows(List<Show> shows) {
        for (Show show : shows) {
            Show savedShow = showRepository.save(show);
            List<TheaterSeat> theaterSeats = theaterSeatRepository.findByTheater(savedShow.getTheater());
            for (TheaterSeat theaterSeat : theaterSeats) {
                ShowSeat showSeat = new ShowSeat();
                showSeat.setShow(savedShow);
                showSeat.setTheaterSeat(theaterSeat);
                showSeatRepository.save(showSeat);
            }
        }
    }

    public void addTheaterSeats(List<TheaterSeat> theaterSeats) {
        for (TheaterSeat theaterSeat : theaterSeats) {
            theaterSeatRepository.save(theaterSeat);
        }
    }

    public void addCustomers(List<Customer> customers) {
        for (Customer customer : customers) {
            customerRepository.save(customer);
        }
    }

    @Transactional
    //We need transactions because we are updating 3 tables: OFFER, THEATER & THEATER_OFFERS
    public void addOffers(List<Offer> offers) {
        for (Offer offer : offers) {
            Offer persistedOffer = em.merge(offer);
            List<Theater> theaters = persistedOffer.getTheaters();
            for (int i = 0; i < theaters.size(); i++) {
                Theater theater = theaters.get(i);
                theater.getOffers().add(persistedOffer);
                em.merge(theater);
            }
        }
    }
}
