package com.example.bookmyticket.api;

import com.example.bookmyticket.dto.*;
import com.example.bookmyticket.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping(path = "/theaters", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addTheater(@RequestBody List<TheaterDTO> theaters){
        adminService.addTheaters(theaters);
    }

    @PostMapping(path = "/theaterSeats", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addTheaterSeats(@RequestBody List<TheaterSeatDTO> theaterSeats){
        adminService.addTheaterSeats(theaterSeats);
    }

    @PostMapping(path = "/movies", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addMovies(@RequestBody List<MovieDTO> movies){
        adminService.addMovies(movies);
    }

    @PostMapping(path = "/shows", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addShows(@RequestBody List<ShowDTO> shows){
        adminService.addShows(shows);
    }

    @PostMapping(path = "/customers", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addCustomers(@RequestBody List<CustomerDTO> customerList){
        adminService.addCustomers(customerList);
    }

    @PostMapping(path = "/offers")
    @ResponseStatus(HttpStatus.CREATED)
    public void addOffers(@RequestBody List<OfferDTO> offers){
        adminService.addOffers(offers);
    }
}
