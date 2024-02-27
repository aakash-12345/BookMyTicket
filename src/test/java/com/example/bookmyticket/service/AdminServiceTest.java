package com.example.bookmyticket.service;

import com.example.bookmyticket.repos.*;
import com.example.bookmyticket.dto.*;
import com.example.bookmyticket.dao.Show;
import com.example.bookmyticket.dao.ShowSeat;
import com.example.bookmyticket.dao.Theater;
import com.example.bookmyticket.dao.TheaterSeat;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor
public class AdminServiceTest {
    @Mock
    private TheaterRepository theaterRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private TheaterSeatRepository theaterSeatRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ShowSeatRepository showSeatRepository;

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddTheaters() {
        List<TheaterDTO> theaterDTOs = Arrays.asList(
                new TheaterDTO(1L, "Theater1", "City1"),
                new TheaterDTO(2L, "Theater2", "City2")
        );

        List<Theater> expectedTheaters = theaterDTOs.stream()
                .map(theaterDTO -> Theater.builder()
                        .theaterId(theaterDTO.getTheaterId())
                        .theaterName(theaterDTO.getTheaterName())
                        .theaterCity(theaterDTO.getTheaterCity())
                        .build())
                .collect(Collectors.toList());

        adminService.addTheaters(theaterDTOs);

        verify(theaterRepository, Mockito.times(1)).saveAll(expectedTheaters);
    }

    @Test
    void testAddShows() {

        List<ShowDTO> validShowDTOs = new ArrayList<>();
        ShowDTO showDTO = ShowDTO.builder()
                .movieId(1L)
                .showId(1L)
                .theaterId(100L)
                .movieName("MovieName")
                .showDate(LocalDate.now()).build();
        validShowDTOs.add(showDTO);
        List<TheaterSeat> theaterSeats = new ArrayList<>();
        TheaterSeat theaterSeat = TheaterSeat.builder()
                .theaterSeatId(1L)
                .theaterId(100L)
                .seatPrice(BigDecimal.valueOf(100))
                .seatType("Basic")
                .build();
        theaterSeats.add(theaterSeat);
        when(theaterSeatRepository.findByTheaterId(anyLong())).thenReturn(theaterSeats);
        when(showRepository.save(any(Show.class))).thenReturn(new Show());
        when(showSeatRepository.save(any(ShowSeat.class))).thenReturn(new ShowSeat());

        adminService.addShows(validShowDTOs);

        verify(theaterSeatRepository, times(validShowDTOs.size())).findByTheaterId(anyLong());
        verify(showRepository, times(validShowDTOs.size())).save(any(Show.class));
        verify(showSeatRepository, times(validShowDTOs.size() * theaterSeats.size())).save(any(ShowSeat.class));
    }
    @Test
    void testAddMovies() {

        List<MovieDTO> validMovieDTOs = new ArrayList<>();
        MovieDTO movieDTO = MovieDTO.builder()
                .movieId(1L)
                .runTime(165L)
                .movieName("MovieName").build();
        validMovieDTOs.add(movieDTO);
        when(movieRepository.saveAll(anyIterable())).thenReturn(new ArrayList<>());

        adminService.addMovies(validMovieDTOs);

        verify(movieRepository).saveAll(anyIterable());
    }
    @Test
    void testAddTheaterSeats() {

        List<TheaterSeatDTO> validTheaterSeatDTOs = new ArrayList<>();
        TheaterSeatDTO theaterSeatDTO = TheaterSeatDTO.builder()
                .theaterSeatId(1L)
                .theaterId(100L)
                .seatPrice(BigDecimal.valueOf(100))
                .seatType("Basic").build();
        validTheaterSeatDTOs.add(theaterSeatDTO);
        when(theaterSeatRepository.saveAll(anyIterable())).thenReturn(new ArrayList<>());

        adminService.addTheaterSeats(validTheaterSeatDTOs);

        verify(theaterSeatRepository).saveAll(anyIterable());
    }
    @Test
    void testAddCustomers() {

        List<CustomerDTO> validCustomerDTOs = new ArrayList<>();
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerId(1L)
                .customerName("CustomerName").build();
        validCustomerDTOs.add(customerDTO);
        when(customerRepository.saveAll(anyIterable())).thenReturn(new ArrayList<>());

        adminService.addCustomers(validCustomerDTOs);

        verify(customerRepository).saveAll(anyIterable());
    }
    @Test
    void testAddOffers() {

        List<OfferDTO> validOfferDTOs = new ArrayList<>();
        OfferDTO offerDTO = OfferDTO.builder()
                .offerId(1L)
                .offerDiscount(BigDecimal.valueOf(0.2))
                .offerEndDate(LocalDate.now().plusDays(10))
                .offerStartDate(LocalDate.now())
                .offerName("OfferName")
                .build();
        validOfferDTOs.add(offerDTO);
        when(offerRepository.saveAll(anyIterable())).thenReturn(new ArrayList<>());

        adminService.addOffers(validOfferDTOs);

        verify(offerRepository).saveAll(anyIterable());
    }

}
