package com.foodtaste.serviceImpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.foodtaste.exception.CartException;
import com.foodtaste.exception.UserException;
import com.foodtaste.model.Cart;
import com.foodtaste.model.CartItem;
import com.foodtaste.model.MenuItem;
import com.foodtaste.model.User;
import com.foodtaste.repository.CartItemRepository;
import com.foodtaste.repository.CartRepository;
import com.foodtaste.repository.UserRepository;
import com.foodtaste.service.CartService;

public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Cart getActiveCart(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserException("User not found with id: " + userId));

		Cart cart = cartRepository.findByUserAndCheckedOutFalse(user).orElse(null);

		if (cart == null) {
			cart = new Cart();
			cart.setUser(user);
			cart.setCheckout(false);
			cart.setTotalAmount(BigDecimal.ZERO);
			cart = cartRepository.save(cart);
		}
		return cart;
	}

	@Override
	public Cart addItemToCart(String userId, MenuItem menuItem) {
		Cart cart = getActiveCart(userId);

		List<CartItem> items = cart.getCardItem();

		CartItem existingItem = items.stream().filter(item -> item.getMenuItem().getId().equals(menuItem.getId()))
				.findFirst().orElse(null);

		if (existingItem != null) {
			int newQty = existingItem.getQuantity() + 1;
			existingItem.setQuantity(newQty);
			existingItem.setSubTotal(menuItem.getPrice().multiply(BigDecimal.valueOf(newQty)));
			cartItemRepository.save(existingItem);
		} else {
			CartItem newCartItem = new CartItem();
			newCartItem.setCart(cart);
			newCartItem.setMenuItem(menuItem);
			newCartItem.setQuantity(1);
			newCartItem.setSubTotal(menuItem.getPrice());
			items.add(newCartItem);
			cartItemRepository.save(newCartItem);
		}

		BigDecimal totalAmout = items.stream().map(CartItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
		cart.setTotalAmount(totalAmout);
		return cartRepository.save(cart);
	}

	@Override
	public Cart updateCartItem(String userId, Integer menuItemId, int newQuantity) {
		Cart cart = getActiveCart(userId);
		List<CartItem> items = cart.getCardItem();

		CartItem existingItem = items.stream().filter(item -> item.getMenuItem().getId().equals(menuItemId)).findFirst()
				.orElseThrow(() -> new CartException("Item not found in cart"));

		if (newQuantity <= 0) {
			items.remove(existingItem);
			cartItemRepository.delete(existingItem);
		} else {
			existingItem.setQuantity(newQuantity);
			existingItem.setSubTotal(existingItem.getMenuItem().getPrice().multiply(BigDecimal.valueOf(newQuantity)));
			cartItemRepository.save(existingItem);
		}

		BigDecimal totalAmount = items.stream().map(CartItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
		cart.setTotalAmount(totalAmount);
		return cartRepository.save(cart);
	}

	@Override
	public Cart getCartDetails(String userId) {
		return getActiveCart(userId);
	}

	@Override
	public Cart clearCart(String userId) {
		Cart cart = getActiveCart(userId);
		if (cart.getCardItem() != null) {
			cart.getCardItem().clear();
		}
		cart.setTotalAmount(BigDecimal.ZERO);
		return cartRepository.save(cart);
	}

}
