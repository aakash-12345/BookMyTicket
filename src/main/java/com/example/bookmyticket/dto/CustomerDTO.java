package com.example.bookmyticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CustomerDTO {
    private Long customerId;
    private String customerName;
    private String customerUserName;
    private String customerPassword;
    private String customerRoles;
}
