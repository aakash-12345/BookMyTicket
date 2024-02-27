package com.example.bookmyticket.scheduler;

import com.example.bookmyticket.dao.Refund;
import com.example.bookmyticket.repos.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RefundBookings {

    private final RefundRepository refundRepository;

    @Scheduled(fixedRateString = "${refund.check.frequency}")
    public void refundAllCancelledBookings() {
        List<Refund> refundList = refundRepository.findAllByIsRefunded(false);
        for (Refund refund : refundList) {
            // Refund the amount to the customer
            refund.setIsRefunded(true);
            refundRepository.save(refund);
        }
    }

}
