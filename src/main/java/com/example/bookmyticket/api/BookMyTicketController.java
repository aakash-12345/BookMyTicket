package com.example.bookmyticket.api;

import com.example.bookmyticket.dao.BookingRequest;
import com.example.bookmyticket.dto.OfferDTO;
import com.example.bookmyticket.dto.ShowDTO;
import com.example.bookmyticket.dto.ShowSeatDTOResponse;
import com.example.bookmyticket.dto.TheaterDTO;
import com.example.bookmyticket.service.BookMyTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/bookMyTicket")
public class BookMyTicketController {

    private final BookMyTicketService bookMyTicketService;

    @GetMapping(path = "/movieShows")
    public List<ShowDTO> movieShows(
            @RequestParam(value = "theaterName") String theaterName,
            @RequestParam(value = "city") String city
    ) {
        return bookMyTicketService.findAllShowsByTheaterNameAndCity(theaterName, city);
    }

    @GetMapping(path = "/getAllTheaters")
    public List<TheaterDTO> getAllTheaters() {
        return bookMyTicketService.getAllTheaters();
    }

    @GetMapping(path = "/availableShowSeats")
    public ShowSeatDTOResponse availableShowSeats(@RequestParam(value = "showId") Long showId) {
        return bookMyTicketService.findAllAvailableSeatsForShow(showId);
    }

    @PostMapping(path = "/reserveSeats")
    public String reserveSeats(@RequestBody BookingRequest bookingRequest) {
        try {
            return bookMyTicketService.reserveSeats(bookingRequest);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping(path = "/confirmSeats")
    public String confirmSeats(
            @RequestBody BookingRequest bookingRequest,
            @RequestParam(value = "offerId", required = false) Long offerId
    ) {
        try {
            offerId = offerId == null ? 0 : offerId;
            return bookMyTicketService.confirmSeats(bookingRequest, offerId);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping(path = "/offerList")
    public List<OfferDTO> offerList() {
        return bookMyTicketService.getOfferList();
    }

    @GetMapping(path = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
////            persistentTokenBasedRememberMeServices.logout(request, response, auth);
//            SecurityContextHolder.getContext().setAuthentication(null);
//        }
//
    }


}
