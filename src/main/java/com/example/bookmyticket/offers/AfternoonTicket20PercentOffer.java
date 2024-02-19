package com.example.bookmyticket.offers;

import com.example.bookmyticket.data.BookingRepository;
import com.example.bookmyticket.model.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
public class AfternoonTicket20PercentOffer implements OfferProcessor {

    public static final double TWENTY_PERCENT_OFF = 0.8;

    private final BookingRepository bookingRepository;

    @Override
    public void process(List<ShowSeat> showSeats, Booking booking) {
        if (CollectionUtils.isEmpty(showSeats)) {
            return;
        }

        Theater theater = showSeats.get(0).getTheaterSeat().getTheater();
        List<Offer> offers = theater.getOffers();
        if (!CollectionUtils.isEmpty(offers) && offers.stream().anyMatch(offer -> offer.getOfferType().equals(OfferType.AFTERNOON_TICKET))) {
            Show show = showSeats.get(0).getShow();
            if (afternoonShow(show)) {
                booking.setTotalAmount(booking.getTotalAmount().multiply(BigDecimal.valueOf(TWENTY_PERCENT_OFF)));
                bookingRepository.save(booking);
            }
        }
    }

    private boolean afternoonShow(Show show) {
        // Afternoon is 12pm to 5pm
        LocalTime startTime = show.getStartTime();
        return startTime.isAfter(LocalTime.parse("11:59")) && startTime.isBefore(LocalTime.parse("17:00"));
    }

    @Override
    public OfferType getOfferType() {
        return OfferType.AFTERNOON_TICKET;
    }

}
