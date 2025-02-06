package com.foodtaste.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {

	private Integer orderId;
	private BigDecimal totalAmount;
	private Integer totalQuantity;
	private LocalDateTime createdAt;
	private List<OrderItemResponse> items;
}
