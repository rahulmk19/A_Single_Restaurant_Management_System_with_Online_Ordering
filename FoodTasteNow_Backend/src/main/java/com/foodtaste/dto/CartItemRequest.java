package com.foodtaste.dto;

import lombok.Data;

@Data
public class CartItemRequest {

	private Integer menuItemId;
	private int quantity;
}
