package com.example.bookmyticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookMyTicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookMyTicketApplication.class, args);
    }

}
