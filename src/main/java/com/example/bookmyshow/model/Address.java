package com.example.bookmyshow.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@Data
public class Address {

    private String street;
    private String pincode;
}