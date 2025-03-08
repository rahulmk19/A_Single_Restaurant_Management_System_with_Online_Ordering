package com.foodtaste.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.foodtaste.constant.AppConstants;
import com.foodtaste.dto.CartResponse;
import com.foodtaste.model.Cart;
import com.foodtaste.service.CartService;

@RestController
@RequestMapping(AppConstants.APP_NAME + "/cart" + AppConstants.USER)
public class CartController {

	@Autowired
	private CartService cartService;

	@GetMapping
	public ResponseEntity<CartResponse> getCart() {
		CartResponse cart = cartService.getCart();
		return ResponseEntity.ok(cart);
	}

	@PostMapping(AppConstants.SAVE + "/{menuItemId}")
	public ResponseEntity<Cart> addItemToCart(@PathVariable Integer menuItemId) {
		Cart cart = cartService.addItemToCart(menuItemId);
		return ResponseEntity.ok(cart);
	}

	@DeleteMapping("/remove/{menuItemId}")
	public ResponseEntity<Cart> removeItemFromCart(@PathVariable Integer menuItemId) {
		Cart cart = cartService.removeItemFromCart(menuItemId);
		return ResponseEntity.ok(cart);
	}

	@PutMapping("/update/{menuItemId}")
	public ResponseEntity<Cart> updateCartItemQuantity(@PathVariable Integer menuItemId,
			@RequestParam("quantity") int newQuantity) {
		Cart cart = cartService.updateCartItemQuantity(menuItemId, newQuantity);
		return ResponseEntity.ok(cart);
	}
}
