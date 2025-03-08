package com.foodtaste.service;

import com.foodtaste.dto.CartResponse;
import com.foodtaste.model.Cart;
import com.foodtaste.model.MenuItem;
import com.foodtaste.model.User;

public interface CartService {

	CartResponse getCart();

	Cart addItemToCart(Integer menuItemId);

	Cart removeItemFromCart(Integer menuItemId);

	Cart updateCartItemQuantity(Integer menuItemId, int newQuantity);

}
