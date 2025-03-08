package com.foodtaste.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {

	private String menuItemName;
	private Integer quantity;
	private BigDecimal subTotal;
}
