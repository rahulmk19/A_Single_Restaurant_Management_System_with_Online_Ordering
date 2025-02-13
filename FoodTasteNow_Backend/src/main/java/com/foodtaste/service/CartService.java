package com.foodtaste.service;

import com.foodtaste.model.Cart;
import com.foodtaste.model.MenuItem;

public interface CartService {

	Cart getActiveCart(String userId);

	Cart addItemToCart(String userId, MenuItem menuItem);

	Cart updateCartItem(String userId, Integer menuItemId, int newQuantity);

	Cart getCartDetails(String userId);

	Cart clearCart(String userId);

}
