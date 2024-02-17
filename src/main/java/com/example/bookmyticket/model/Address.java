package com.example.bookmyticket.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Address {

    private String street;
    private String pincode;
}