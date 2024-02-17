package com.example.bookmyticket.offers;

import com.example.bookmyticket.model.Booking;
import com.example.bookmyticket.model.ShowSeat;

import java.util.List;

public class OfferProcessorManager {

    List<OfferProcessor> offerProcessors;

    public OfferProcessorManager(List<OfferProcessor> offerProcessors) {
        this.offerProcessors = offerProcessors;
    }

    public void process(List<ShowSeat> showSeats, Booking booking) {
        offerProcessors.forEach(offerProcessor -> offerProcessor.process(showSeats, booking));
    }
}
