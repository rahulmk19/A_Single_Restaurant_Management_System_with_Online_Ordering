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

import com.foodtaste.dto.OrderRequest;
import com.foodtaste.dto.OrderResponse;
import com.foodtaste.enums.StatusEnum;
import com.foodtaste.model.OrderDetail;
import com.foodtaste.service.OrderDetailService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

	@Autowired
	private OrderDetailService orderDetailService;

	@PostMapping("{id}")
	public ResponseEntity<OrderResponse> placeOrder(@PathVariable Integer id,
			@Valid @RequestBody OrderRequest request) {
		OrderResponse response = orderDetailService.createOrder(id, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderDetail> getOrderDetailsById(@PathVariable Integer id) {
		OrderDetail response = orderDetailService.getOrderById(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/with-items/{id}")
	public ResponseEntity<OrderDetail> getOrderDetailsWithItemsById(@PathVariable Integer id) {
		OrderDetail response = orderDetailService.getOrderByIdWithItems(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<List<OrderDetail>> getAllOrders() {
		List<OrderDetail> response = orderDetailService.getAllOrders();
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<OrderDetail> updateOrderStatus(@PathVariable Integer id, @RequestBody StatusEnum status) {
		OrderDetail updateOrder = orderDetailService.updateOrderStatus(id, status);
		return ResponseEntity.ok(updateOrder);
	}

	@PatchMapping("/cancel/{id}")
	public ResponseEntity<OrderDetail> cancelOrderStatus(@PathVariable Integer id) {
		log.debug("Received request to cancel order with ID: {}", id);

		OrderDetail updatedOrder = orderDetailService.cancelOrderById(id);
		return ResponseEntity.ok(updatedOrder);
	}

}
