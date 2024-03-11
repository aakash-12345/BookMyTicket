package com.example.bookmyticket.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @Column(name = "CUSTOMER_ID", nullable = false)
    private Long customerId;

    private String customerName;

    private String customerUserName;

    private String customerPassword;

    private String customerRoles;
}