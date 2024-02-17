package com.example.bookmyticket.offers;

import com.example.bookmyticket.model.Booking;
import com.example.bookmyticket.model.ShowSeat;

import java.util.List;

public interface OfferProcessor {

    public void process(List<ShowSeat> showSeats, Booking booking);

}
