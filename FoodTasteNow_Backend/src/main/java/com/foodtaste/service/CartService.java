package com.foodtaste.service;

import com.foodtaste.model.Cart;
import com.foodtaste.model.MenuItem;

public interface CartService {

	Cart addItemToCart(String userId, Integer menuItemId, int quantity);
	
	void removeItemFromCart(String userId, Integer cartItemId);
	
	Cart checkoutCart(String userId);

	Cart updateCartItem(String userId, Integer menuItemId, int newQuantity);

	Cart clearCart(String userId);
	
	Cart getActiveCart(String userId);

}
