package com.foodtaste.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.foodtaste.dto.CartItemRequest;
import com.foodtaste.model.Cart;
import com.foodtaste.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@PostMapping("/items")
	public ResponseEntity<Cart> addItemToCart(@RequestBody CartItemRequest request, Authentication authentication) {

		String userId = authentication.getName();
		Cart cart = cartService.addItemToCart(userId, request.getMenuItemId(), request.getQuantity());
		return ResponseEntity.ok(cart);
	}

	@DeleteMapping("/items/{cartItemId}")
	public ResponseEntity<String> removeItemFromCart(@PathVariable Integer cartItemId, Authentication authentication) {
		String userId = authentication.getName();
		cartService.removeItemFromCart(userId, cartItemId);
		return ResponseEntity.ok("Item removed successfully");
	}

	@GetMapping
	public ResponseEntity<Cart> getCart(Authentication authentication) {
		String userId = authentication.getName();
		Cart cart = cartService.getActiveCart(userId);
		return ResponseEntity.ok(cart);
	}

	@PostMapping("/checkout")
	public ResponseEntity<Cart> checkoutCart(Authentication authentication) {
		String userId = authentication.getName();
		Cart cart = cartService.checkoutCart(userId);
		return ResponseEntity.ok(cart);
	}
}
