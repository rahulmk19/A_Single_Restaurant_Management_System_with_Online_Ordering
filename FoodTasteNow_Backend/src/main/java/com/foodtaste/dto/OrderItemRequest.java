package com.foodtaste.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {

	@NotNull(message = "Menu item ID cannot be null")
	private Integer menuItemId;

	@Min(value = 1, message = "Quantity must be at least 1")
	private Integer quantity;
}
