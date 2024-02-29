package com.example.bookmyticket.repos;

import com.example.bookmyticket.dao.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
    List<Refund> findAllByIsRefunded(Boolean status);
}
