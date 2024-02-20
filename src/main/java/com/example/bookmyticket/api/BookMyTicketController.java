package com.example.bookmyticket.api;

import com.example.bookmyticket.dto.ShowDTO;
import com.example.bookmyticket.dto.ShowSeatDTO;
import com.example.bookmyticket.model.BookingRequest;
import com.example.bookmyticket.model.ShowRequest;
import com.example.bookmyticket.model.ShowSeatRequest;
import com.example.bookmyticket.service.BookMyTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/bms")
public class BookMyTicketController {

    private final BookMyTicketService bookMyTicketService;

    @GetMapping(path = "/movieShows", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ShowDTO> shows(@RequestParam(value = "theaterName") String theaterName, @RequestParam(value = "city") String city) {
        return bookMyTicketService.findAllShowsByTheaterNameAndCity(theaterName, city);
    }

    @GetMapping(path = "/showSeats", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ShowSeatDTO> showSeats(@RequestParam(value = "showId") Long showId) {
        return bookMyTicketService.findAllAvailableSeatsForShow(showId);
    }

    @PostMapping(path = "/reserveSeats", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String bookSeats(@RequestBody BookingRequest bookingRequest) {
        return bookMyTicketService.reserveSeats(bookingRequest);
    }

    @PostMapping(path = "/confirmseats", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String conSeats(@RequestBody BookingRequest bookingRequest) {
        return bookMyTicketService.confirmSeats(bookingRequest);
    }

}
