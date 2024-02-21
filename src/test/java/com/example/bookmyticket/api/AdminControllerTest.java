package com.example.bookmyticket.api;

import com.example.bookmyticket.BookMyTicketApplication;
import com.example.bookmyticket.BookMyTicketApplicationTests;
import com.example.bookmyticket.dto.*;
import com.example.bookmyticket.model.Offer;
import com.example.bookmyticket.model.Show;
import com.example.bookmyticket.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        //write test case for addTheater method of Admin controller
        List<TheaterDTO> theaters = Arrays.asList(new TheaterDTO(100L, "name1", "city1"), new TheaterDTO(101L, "name2", "city2"));

        // When
        adminController.addTheater(theaters);

        // Then
        verify(adminService, times(1)).addTheaters(theaters);

    }
    @Test
    public void testAddTheaterSeats() {
        //write test case for addTheater method of Admin controller
        List<TheaterSeatDTO> theaterSeats = Arrays.asList(
                new TheaterSeatDTO(1L, 100L, "Basic", BigDecimal.valueOf(100L)),
                new TheaterSeatDTO(2L, 100L, "Premium", BigDecimal.valueOf(200L)));

        // When
        adminController.addTheaterSeats(theaterSeats);

        // Then
        verify(adminService, times(1)).addTheaterSeats(theaterSeats);

    }
    @Test
    public void testAddMovies() {
        //write test case for addTheater method of Admin controller
        List<MovieDTO> movies = Arrays.asList(
                new MovieDTO(100L, "movie1"),
                new MovieDTO(101L, "movie2"));

        // When
        adminController.addMovies(movies);

        // Then
        verify(adminService, times(1)).addMovies(movies);

    }
    @Test
    public void testAddShows() {
        //write test case for addTheater method of Admin controller
        List<ShowDTO> shows = Arrays.asList(
                new ShowDTO(100L, LocalDate.now(), "10:00", 165L, 1L, 1L, "movie1"),
                new ShowDTO(101L, LocalDate.now(), "14:00", 165L, 1L, 1L, "movie1"));

        // When
        adminController.addShows(shows);

        // Then
        verify(adminService, times(1)).addShows(shows);

    }
    @Test
    public void testCustomer() {
        //write test case for addTheater method of Admin controller
        List<CustomerDTO> customers = Arrays.asList(
                new CustomerDTO(100L, "name1"),
                new CustomerDTO(101L, "name2"));

        // When
        adminController.addCustomers(customers);

        // Then
        verify(adminService, times(1)).addCustomers(customers);

    }
    @Test
    public void testAddOffers() {
        //write test case for addTheater method of Admin controller
        List<OfferDTO> offers = Arrays.asList(
                new OfferDTO(1L, "offer1", BigDecimal.valueOf(0.1), LocalDate.now(), LocalDate.now().plusDays(7)),
                new OfferDTO(2L, "offer2", BigDecimal.valueOf(0.2), LocalDate.now(), LocalDate.now().plusDays(10)));

        // When
        adminController.addOffers(offers);

        // Then
        verify(adminService, times(1)).addOffers(offers);

    }
}
