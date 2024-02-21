package com.example.bookmyticket.repos;

import com.example.bookmyticket.dao.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}