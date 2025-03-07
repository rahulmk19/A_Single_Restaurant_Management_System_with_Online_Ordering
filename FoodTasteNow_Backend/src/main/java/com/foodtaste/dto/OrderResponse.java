package com.foodtaste.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodtaste.enums.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

	private Integer orderId;
	private Long userId;
	private BigDecimal totalAmount;
	private Integer totalQuantity;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime createdAt;
	private String customerName;
	private String customerAddress;
	private String contactNum;
	private String alternateContactNum;
	private StatusEnum status;
	private List<OrderItemResponse> items;

}
