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

    @GetMapping(path = "/movieshows", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ShowDTO> shows(@RequestBody ShowRequest showRequest) {
        return bookMyTicketService.findAllShowsByMovieAndDateAndCity(showRequest.moviename, showRequest.date, showRequest.city);
    }

    @GetMapping(path = "/showseats", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ShowSeatDTO> showSeats(@RequestBody ShowSeatRequest showSeatRequest) {
        return bookMyTicketService.findAllAvailableSeatsForShow(showSeatRequest.getShowid());
    }

    @PostMapping(path = "/reserveseats", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String bookSeats(@RequestBody BookingRequest bookingRequest) {
        return bookMyTicketService.reserveSeats(bookingRequest);
    }

    @PostMapping(path = "/confirmseats", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String conSeats(@RequestBody BookingRequest bookingRequest) {
        return bookMyTicketService.confirmSeats(bookingRequest);
    }

}
