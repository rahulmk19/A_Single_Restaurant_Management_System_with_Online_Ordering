package com.foodtaste.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodtaste.constant.AppConstants;
import com.foodtaste.dto.CartResponse;
import com.foodtaste.dto.CheckoutOrderRequest;
import com.foodtaste.dto.OrderRequest;
import com.foodtaste.dto.OrderResponse;
import com.foodtaste.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(AppConstants.APP_NAME + "/cart" + AppConstants.USER)
@CrossOrigin(origins = "*")
public class CartController {

	@Autowired
	private CartService cartService;

	@GetMapping
	public ResponseEntity<CartResponse> getCart() {
		CartResponse cart = cartService.getCart();
		return ResponseEntity.ok(cart);
	}

	@PostMapping(AppConstants.SAVE + "/{menuItemId}")
	public ResponseEntity<CartResponse> addItemToCart(@PathVariable Integer menuItemId) {
		CartResponse cartResponse = cartService.addItemToCart(menuItemId);
		return ResponseEntity.ok(cartResponse);
	}

	@DeleteMapping(AppConstants.DELETE + "/{menuItemId}")
	public ResponseEntity<CartResponse> removeItemFromCart(@PathVariable Integer menuItemId) {
		CartResponse cart = cartService.removeItemFromCart(menuItemId);
		return ResponseEntity.ok(cart);
	}

	@PatchMapping(AppConstants.UPDATE + "/{menuItemId}/{quantity}")
	public ResponseEntity<CartResponse> updateCartItemQuantity(@PathVariable Integer menuItemId,
			@PathVariable("quantity") int newQuantity) {
		CartResponse cart = cartService.updateCartItemQuantity(menuItemId, newQuantity);
		return ResponseEntity.ok(cart);
	}

	@PostMapping("/placedorder")
	public ResponseEntity<OrderResponse> placedOrder(@Valid @RequestBody CheckoutOrderRequest orderRequest) {
		OrderResponse orderPlaced = cartService.placedOrder(orderRequest);
		return new ResponseEntity<>(orderPlaced, HttpStatus.OK);
	}
}
