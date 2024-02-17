package com.example.bookmyticket.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ShowRequest {
    public String moviename;
    public LocalDate date;
    public String city;

}
