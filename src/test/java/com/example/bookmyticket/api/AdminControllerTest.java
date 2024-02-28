package com.example.bookmyticket.api;

import com.example.bookmyticket.dto.*;
import com.example.bookmyticket.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testAddTheater() {
        List<TheaterDTO> theaters = Arrays.asList(new TheaterDTO(100L, "name1", "city1"), new TheaterDTO(101L, "name2", "city2"));

        adminController.addTheater(theaters);

        verify(adminService, times(1)).addTheaters(theaters);

    }

    @Test
    public void testAddTheaterSeats() {
        List<TheaterSeatDTO> theaterSeats = Arrays.asList(
                new TheaterSeatDTO(1L, 100L, "Basic", BigDecimal.valueOf(100L)),
                new TheaterSeatDTO(2L, 100L, "Premium", BigDecimal.valueOf(200L)));

        adminController.addTheaterSeats(theaterSeats);

        verify(adminService, times(1)).addTheaterSeats(theaterSeats);

    }

    @Test
    public void testAddMovies() {
        List<MovieDTO> movies = Arrays.asList(
                new MovieDTO(100L, "movie1", 165L),
                new MovieDTO(101L, "movie2", 150L));

        adminController.addMovies(movies);

        verify(adminService, times(1)).addMovies(movies);

    }

    @Test
    public void testAddShows() {
        List<ShowDTO> shows = Arrays.asList(
                new ShowDTO(100L, LocalDate.now(), "10:00", 1L, 1L, "movie1"),
                new ShowDTO(101L, LocalDate.now(), "14:00", 1L, 1L, "movie1"));

        adminController.addShows(shows);

        verify(adminService, times(1)).addShows(shows);

    }

    @Test
    public void testCustomer() {
        List<CustomerDTO> customers = Arrays.asList(
                new CustomerDTO(100L, "name1"),
                new CustomerDTO(101L, "name2"));

        adminController.addCustomers(customers);

        verify(adminService, times(1)).addCustomers(customers);

    }

    @Test
    public void testAddOffers() {
        List<OfferDTO> offers = Arrays.asList(
                new OfferDTO(1L, "offer1", BigDecimal.valueOf(0.1), LocalDate.now(), LocalDate.now().plusDays(7)),
                new OfferDTO(2L, "offer2", BigDecimal.valueOf(0.2), LocalDate.now(), LocalDate.now().plusDays(10)));

        adminController.addOffers(offers);

        verify(adminService, times(1)).addOffers(offers);

    }
}
