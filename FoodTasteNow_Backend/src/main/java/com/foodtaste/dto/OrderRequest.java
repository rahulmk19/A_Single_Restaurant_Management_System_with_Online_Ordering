package com.foodtaste.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OrderRequest {

	private String customerName;
	private String customerAddress;
	private String contactNum;
	private String alternateContactNum;

	@NotEmpty(message = "Order items cannot be empty")
	private List<OrderItemRequest> items;
}
