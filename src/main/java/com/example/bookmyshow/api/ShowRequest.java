package com.example.bookmyshow.api;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
public class ShowRequest {
    String moviename;
    LocalDate date;
    String city;

}
