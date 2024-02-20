package com.example.bookmyticket;

import com.example.bookmyticket.swagger.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableSwagger
public class BookMyTicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookMyTicketApplication.class, args);
    }

}
