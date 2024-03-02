package com.example.bookmyticket.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceExceptionHandlerTest {

    @InjectMocks
    private ServiceExceptionHandler serviceExceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testHandleInvalidDataException() {

        ConstraintViolationException ex = new ConstraintViolationException("Invalid data", null, null);

        ServiceExceptionHandler.ErrorResponse response = serviceExceptionHandler.handleInvalidDataException(ex);

        assertEquals("Invalid data", response.getMessage());
    }

    @Test
    void testHandleInvalidBookingException() {

        InvalidBookingException ex = new InvalidBookingException("Invalid booking");

        ServiceExceptionHandler.ErrorResponse response = serviceExceptionHandler.handleInvalidBookingException(ex);

        assertEquals("Invalid booking", response.getMessage());
    }

    @Test
    void testHandlePaymentFailedException() {

        PaymentFailedException ex = new PaymentFailedException("Payment failed");

        ServiceExceptionHandler.ErrorResponse response = serviceExceptionHandler.handlePaymentFailedException(ex);

        assertEquals("Payment failed", response.getMessage());
    }

    @Test
    void testHandleSeatUnavailableException() {

        SeatUnavailableException ex = new SeatUnavailableException("Seat Unavailable");

        ServiceExceptionHandler.ErrorResponse response = serviceExceptionHandler.handleSeatUnavailableException(ex);

        assertEquals("Seat Unavailable", response.getMessage());
    }

    @Test
    void testHandleCustomerNotFoundException() {

        CustomerNotFoundException ex = new CustomerNotFoundException("Customer not found");

        ServiceExceptionHandler.ErrorResponse response = serviceExceptionHandler.handleCustomerNotFoundException(ex);

        assertEquals("Customer not found", response.getMessage());
    }

    @Test
    void testHandleReservationExpiredException() {

        ReservationExpiredException ex = new ReservationExpiredException("Reservation Failed");

        ServiceExceptionHandler.ErrorResponse response = serviceExceptionHandler.handleReservationExpiredException(ex);

        assertEquals("Reservation Failed", response.getMessage());
    }

}