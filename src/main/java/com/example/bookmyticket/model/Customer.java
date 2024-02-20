package com.example.bookmyticket.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "customer")
@Data
public class Customer {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="customer_seq")
    @SequenceGenerator(name = "customer_seq", sequenceName = "customer_seq", initialValue = 1, allocationSize=1)
    private Long customerId;

    private String customerName;
}