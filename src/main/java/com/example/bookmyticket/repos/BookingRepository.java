package com.example.bookmyticket.repos;

import com.example.bookmyticket.dao.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}