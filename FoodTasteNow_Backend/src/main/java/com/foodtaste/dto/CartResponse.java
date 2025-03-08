package com.foodtaste.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.foodtaste.model.CartItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartResponse {

	private Integer id;
	private Long userId;
	private List<CartItem> cartItem = new ArrayList<>();
	private Integer totalQty;
	private BigDecimal totalAmount = BigDecimal.ZERO;
}
