package com.foodtaste.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CheckoutOrderRequest {

    @NotEmpty(message = "Customer name cannot be empty")
    private String customerName;

    @NotEmpty(message = "Customer address cannot be empty")
    private String customerAddress;

    @NotEmpty(message = "Contact number cannot be empty")
    private String contactNum;

    @NotEmpty(message = "Alternate contact number cannot be empty")
    private String alternateContactNum;
}

