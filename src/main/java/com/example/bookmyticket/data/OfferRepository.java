package com.example.bookmyticket.data;

import com.example.bookmyticket.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    @Query("select Offer from Offer where :currDate between offerStartDate and offerEndDate")
    List<Offer> findAllValidOffers(LocalDate currDate);

}