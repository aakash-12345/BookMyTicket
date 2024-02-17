package com.example.bookmyshow.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
public class ShowRequest {
    public String moviename;
    public LocalDate date;
    public String city;

}
