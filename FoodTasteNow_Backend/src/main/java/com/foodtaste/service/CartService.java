package com.foodtaste.service;

import com.foodtaste.dto.CartResponse;
import com.foodtaste.dto.OrderResponse;

public interface CartService {

	CartResponse getCart();

	CartResponse addItemToCart(Integer menuItemId);

	CartResponse removeItemFromCart(Integer menuItemId);

	CartResponse updateCartItemQuantity(Integer menuItemId, int newQuantity);
	
	OrderResponse placedOrder();

}
