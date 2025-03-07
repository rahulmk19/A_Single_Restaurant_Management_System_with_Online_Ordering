package com.foodtaste.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodtaste.constant.AppConstants;
import com.foodtaste.dto.OrderRequest;
import com.foodtaste.dto.OrderResponse;
import com.foodtaste.enums.StatusEnum;
import com.foodtaste.model.OrderDetail;
import com.foodtaste.service.OrderDetailService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(AppConstants.APP_NAME + "/orders")
@Slf4j
public class OrderController {

	@Autowired
	private OrderDetailService orderDetailService;

	@PostMapping(AppConstants.SAVE)
	public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest request) {
		OrderResponse response = orderDetailService.createOrder(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping(AppConstants.ADMIN + AppConstants.GET_BY_ID + "/{id}")
	public ResponseEntity<OrderResponse> getOrderDetailsById(@PathVariable Integer id) {
		log.info("Received request to fetch order details with ID: {}", id);
		OrderResponse response = orderDetailService.getOrderById(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/with-items/{id}")
	public ResponseEntity<OrderDetail> getOrderDetailsWithItemsById(@PathVariable Integer id) {
		log.info("Received request to fetch order details with items for order ID: {}", id);
		OrderDetail response = orderDetailService.getOrderByIdWithItems(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping(AppConstants.GET_ALL)
	public ResponseEntity<List<OrderDetail>> getAllOrders() {
		log.info("Received request to fetch all orders");
		List<OrderDetail> response = orderDetailService.getAllOrders();
		return ResponseEntity.ok(response);
	}

	@PatchMapping(AppConstants.UPDATE + "/{id}/status")
	public ResponseEntity<OrderDetail> updateOrderStatus(@PathVariable Integer id, @RequestBody StatusEnum status) {
		log.info("Received request to update status of order with ID: {}", id);
		OrderDetail updatedOrder = orderDetailService.updateOrderStatus(id, status);
		return ResponseEntity.ok(updatedOrder);
	}

	@PatchMapping("/cancel/{id}")
	public ResponseEntity<OrderDetail> cancelOrderStatus(@PathVariable Integer id) {
		log.info("Received request to cancel order with ID: {}", id);
		OrderDetail updatedOrder = orderDetailService.cancelOrderById(id);
		return ResponseEntity.ok(updatedOrder);
	}
}
