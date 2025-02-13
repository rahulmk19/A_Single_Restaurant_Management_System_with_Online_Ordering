package com.foodtaste.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {

//	@NotNull
//	private Integer userId;

	@NotEmpty(message = "Order items cannot be empty")
	private List<OrderItemRequest> items;
}
