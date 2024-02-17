package com.example.bookmyticket.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {
    private Boolean defaultResponse = false;
    private String group = "BookMyTicket";
    private String displayName = "Book My Ticket";
    private String packagesToScan = "com.example.bookmyticket";
    private String apiTitle = "Book My Ticket API";
    private String apiDescription = "Ticket Booking Application";
    private String apiVersion = "v0.0.1";
    private String licenseName = "Apache 2.0";
    private String licenseUrl = "http://springdoc.org";
    private String externalDocDescription = "Book My Ticket App Documentation";
    private String externalDocUrl = "https://springshop.wiki.github.org/docs";
    private String contactTeam = "Aakash Pandit";
    private String contactConfluence = "";
    private String contactEmail = "pandit.aakash3@gmail.com";
}
