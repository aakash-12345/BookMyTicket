package com.example.bookmyticket.api;

import com.example.bookmyticket.dto.ShowDTO;
import com.example.bookmyticket.dto.ShowSeatDTO;
import com.example.bookmyticket.model.BookingRequest;
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

    @GetMapping(path = "/movieShows")
    public List<ShowDTO> shows(@RequestParam(value = "theaterName") String theaterName, @RequestParam(value = "city") String city) {
        return bookMyTicketService.findAllShowsByTheaterNameAndCity(theaterName, city);
    }

    @GetMapping(path = "/showSeats")
    public List<ShowSeatDTO> showSeats(@RequestParam(value = "showId") Long showId) {
        return bookMyTicketService.findAllAvailableSeatsForShow(showId);
    }

    @PostMapping(path = "/reserveSeats")
    public String bookSeats(@RequestBody BookingRequest bookingRequest) {
        return bookMyTicketService.reserveSeats(bookingRequest);
    }

    @PostMapping(path = "/confirmSeats")
    public String confirmSeats(@RequestBody BookingRequest bookingRequest, @RequestParam(value = "offerId") Long offerId) {
        return bookMyTicketService.confirmSeats(bookingRequest, offerId);
    }

    @GetMapping(path = "/offerList")
    public String offerList() {
        return bookMyTicketService.getOfferList();
    }


}
