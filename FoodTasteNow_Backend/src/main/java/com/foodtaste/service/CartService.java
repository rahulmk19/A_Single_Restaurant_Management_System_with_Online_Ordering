package com.foodtaste.service;

import com.foodtaste.model.Cart;
import com.foodtaste.model.MenuItem;

public interface CartService {

	Cart addItemToCart(String userId, Integer menuItemId);
	
	void removeItemFromCart(String userId, Integer cartItemId);
	
	Cart checkoutCart(String userId);

	Cart updateCartItemQuantity(String userId, Integer menuItemId, int newQuantity);

	Cart clearCart(String userId);
	
	Cart getActiveCart(String userId);

}
