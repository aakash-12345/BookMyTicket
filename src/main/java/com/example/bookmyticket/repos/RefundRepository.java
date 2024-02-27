package com.example.bookmyticket.repos;

import com.example.bookmyticket.dao.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long>{

}
