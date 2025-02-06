package com.foodtaste.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponse {

	private String menuItemName;
	private Integer quantity;
	private BigDecimal subTotal;
}
