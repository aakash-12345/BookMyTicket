package com.example.bookmyticket.data;

import com.example.bookmyticket.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {

}