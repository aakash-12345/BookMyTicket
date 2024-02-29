package com.example.bookmyticket.scheduler;

import com.example.bookmyticket.dao.Refund;
import com.example.bookmyticket.repos.RefundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class RefundBookingsTest {
    @InjectMocks
    private RefundBookings refundBookings;

    @Mock
    private RefundRepository refundRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRefundAllCancelledBookings() {
        Refund refund1 = new Refund(1L, 1L, false);
        Refund refund2 = new Refund(2L, 2L, false);
        List<Refund> refundList = Arrays.asList(refund1, refund2);

        Mockito.when(refundRepository.findAllByIsRefunded(false)).thenReturn(refundList);

        refundBookings.refundAllCancelledBookings();

        Mockito.verify(refundRepository, Mockito.times(1)).save(refund1);
        Mockito.verify(refundRepository, Mockito.times(1)).save(refund2);
    }
}
