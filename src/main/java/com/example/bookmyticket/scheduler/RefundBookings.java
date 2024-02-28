package com.example.bookmyticket.scheduler;

import com.example.bookmyticket.dao.Refund;
import com.example.bookmyticket.repos.RefundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
@RequiredArgsConstructor
public class RefundBookings {

    private final RefundRepository refundRepository;

    @Scheduled(fixedRateString = "${refund.check.frequency}")
    public void refundAllCancelledBookings() {
        log.info("Refunding all cancelled bookings scheduler started");
        List<Refund> refundList = refundRepository.findAllByIsRefunded(false);
        for (Refund refund : refundList) {
            log.info("Refunding amount for bookingId: {}", refund.getBookingId());
            // Refund the amount to the customer
            refund.setIsRefunded(true);
            refundRepository.save(refund);
            log.info("Amount refunded successfully");
        }
        log.info("Refunding all cancelled bookings scheduler ended");
    }

}
