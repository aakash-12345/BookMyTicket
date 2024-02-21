package com.example.bookmyticket.service;

import com.example.bookmyticket.data.*;
import com.example.bookmyticket.dto.*;
import com.example.bookmyticket.model.Show;
import com.example.bookmyticket.model.ShowSeat;
import com.example.bookmyticket.model.Theater;
import com.example.bookmyticket.model.TheaterSeat;
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
        // Given
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

        // When
        adminService.addTheaters(theaterDTOs);

        // Then
        verify(theaterRepository, Mockito.times(1)).saveAll(expectedTheaters);
    }

    @Test
    void testAddShows() {
        // Arrange
        List<ShowDTO> validShowDTOs = new ArrayList<>();
        ShowDTO showDTO = ShowDTO.builder()
                .movieId(1L)
                .showId(1L)
                .theaterId(100L)
                .runTime(165L)
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
        when(theaterSeatRepository.findByTheaterId(anyLong())).thenReturn(theaterSeats); // Mock theaterSeatRepository
        when(showRepository.save(any(Show.class))).thenReturn(new Show()); // Mock showRepository save
        when(showSeatRepository.save(any(ShowSeat.class))).thenReturn(new ShowSeat()); // Mock showSeatRepository save

        // Act
        adminService.addShows(validShowDTOs);

        // Assert
        verify(theaterSeatRepository, times(validShowDTOs.size())).findByTheaterId(anyLong()); // Verify theaterSeatRepository method call
        verify(showRepository, times(validShowDTOs.size())).save(any(Show.class)); // Verify showRepository save method call
        verify(showSeatRepository, times(validShowDTOs.size() * theaterSeats.size())).save(any(ShowSeat.class)); // Verify showSeatRepository save method call
    }
    @Test
    void testAddMovies() {
        // Arrange
        List<MovieDTO> validMovieDTOs = new ArrayList<>();
        MovieDTO movieDTO = MovieDTO.builder()
                .movieId(1L)
                .movieName("MovieName").build();
        validMovieDTOs.add(movieDTO);// Create valid MovieDTO objects
        when(movieRepository.saveAll(anyIterable())).thenReturn(new ArrayList<>()); // Mock movieRepository saveAll

        // Act
        adminService.addMovies(validMovieDTOs);

        // Assert
        verify(movieRepository).saveAll(anyIterable()); // Verify movieRepository saveAll method call
    }
    @Test
    void testAddTheaterSeats() {
        // Arrange
        List<TheaterSeatDTO> validTheaterSeatDTOs = new ArrayList<>();
        TheaterSeatDTO theaterSeatDTO = TheaterSeatDTO.builder()
                .theaterSeatId(1L)
                .theaterId(100L)
                .seatPrice(BigDecimal.valueOf(100))
                .seatType("Basic").build();
        validTheaterSeatDTOs.add(theaterSeatDTO);// Create valid MovieDTO objects// Create valid MovieDTO objects
        when(theaterSeatRepository.saveAll(anyIterable())).thenReturn(new ArrayList<>()); // Mock movieRepository saveAll


        // Act
        adminService.addTheaterSeats(validTheaterSeatDTOs);

        // Assert
        verify(theaterSeatRepository).saveAll(anyIterable()); // Verify movieRepository saveAll method call
    }
    @Test
    void testAddCustomers() {
        // Arrange
        List<CustomerDTO> validCustomerDTOs = new ArrayList<>();
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerId(1L)
                .customerName("CustomerName").build();
        validCustomerDTOs.add(customerDTO);// Create valid MovieDTO objects
        when(customerRepository.saveAll(anyIterable())).thenReturn(new ArrayList<>()); // Mock movieRepository saveAll

        // Act
        adminService.addCustomers(validCustomerDTOs);

        // Assert
        verify(customerRepository).saveAll(anyIterable()); // Verify movieRepository saveAll method call
    }
    @Test
    void testAddOffers() {
        // Arrange
        List<OfferDTO> validOfferDTOs = new ArrayList<>();
        OfferDTO offerDTO = OfferDTO.builder()
                .offerId(1L)
                .offerDiscount(BigDecimal.valueOf(0.2))
                .offerEndDate(LocalDate.now().plusDays(10))
                .offerStartDate(LocalDate.now())
                .offerName("OfferName")
                .build();
        validOfferDTOs.add(offerDTO);// Create valid MovieDTO objects
        when(offerRepository.saveAll(anyIterable())).thenReturn(new ArrayList<>()); // Mock movieRepository saveAll

        // Act
        adminService.addOffers(validOfferDTOs);

        // Assert
        verify(offerRepository).saveAll(anyIterable()); // Verify movieRepository saveAll method call
    }

    // Additional test cases can be added for handling invalid input, duplicate show IDs, etc.

}
