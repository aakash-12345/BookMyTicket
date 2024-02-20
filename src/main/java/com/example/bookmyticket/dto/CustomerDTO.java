package com.example.bookmyticket.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
    private Long customerId;
    private String customerName;
}
