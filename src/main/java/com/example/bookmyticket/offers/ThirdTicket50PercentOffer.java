package com.example.bookmyticket.offers;

import com.example.bookmyticket.data.BookingRepository;
import com.example.bookmyticket.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class ThirdTicket50PercentOffer implements OfferProcessor {

    private static final double FIFTY_PERCENT_OFF = 0.5;

    private final BookingRepository bookingRepository;

    @Override
    public void process(List<ShowSeat> showSeats, Booking booking) {
        if (CollectionUtils.isEmpty(showSeats)) {
            return;
        }

        Theater theater = showSeats.get(0).getTheaterSeat().getTheater();
        List<Offer> offers = theater.getOffers();
        if (!CollectionUtils.isEmpty(offers) && offers.stream().anyMatch(offer -> offer.getOfferType().equals(OfferType.THIRD_TICKET))) {
            if (!showSeats.isEmpty()) {
                BigDecimal total = new BigDecimal(0);
                for (int i = 0; i < showSeats.size(); i++) {
                    BigDecimal seatPrice = showSeats.get(i).getTheaterSeat().getSeatPrice();
                    total = total.add((i % 3 == 2) ? seatPrice.multiply(BigDecimal.valueOf(FIFTY_PERCENT_OFF)) : seatPrice);
                }
                booking.setTotalAmount(total);
                bookingRepository.save(booking);
            }
        }
    }

    @Override
    public OfferType getOfferType() {
        return OfferType.THIRD_TICKET;
    }
}