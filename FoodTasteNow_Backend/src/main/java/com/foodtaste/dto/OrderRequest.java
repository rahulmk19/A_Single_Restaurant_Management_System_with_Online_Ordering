package com.foodtaste.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderRequest {

	@NotEmpty
	private String customerName;

	@NotEmpty
	private String customerAddress;

	@NotEmpty
	private String contactNum;

	@NotEmpty
	private String alternateContactNum;

	@NotEmpty(message = "Order items cannot be empty")
	private List<OrderItemRequest> items;
}
