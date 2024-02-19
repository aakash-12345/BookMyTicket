package com.example.bookmyticket.offers;

import com.example.bookmyticket.model.Booking;
import com.example.bookmyticket.model.OfferType;
import com.example.bookmyticket.model.ShowSeat;

import java.util.List;

public interface OfferProcessor {

    void process(List<ShowSeat> showSeats, Booking booking);
    OfferType getOfferType();

}
