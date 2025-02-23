package com.foodtaste.serviceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodtaste.exception.MenuItemException;
import com.foodtaste.exception.UserException;
import com.foodtaste.model.Cart;
import com.foodtaste.model.CartItem;
import com.foodtaste.model.MenuItem;
import com.foodtaste.model.User;
import com.foodtaste.repository.CartItemRepository;
import com.foodtaste.repository.CartRepository;
import com.foodtaste.repository.MenuItemRepository;
import com.foodtaste.repository.UserRepository;
import com.foodtaste.service.CartService;

@Service
@Transactional
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Override
	public Cart addItemToCart(String userId, Integer menuItemId, int quantity) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserException("User not found with id: " + userId));

		Cart cart = cartRepository.findByUserAndCheckout(user, false).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setUser(user);
			newCart.setTotalQty(0);
			newCart.setTotalAmount(BigDecimal.ZERO);
			newCart.setCheckout(false);
			return newCart;
		});

		MenuItem menuItem = menuItemRepository.findById(menuItemId)
				.orElseThrow(() -> new MenuItemException("MenuItem not found with id: " + menuItemId));

		if (menuItem.getQuantity() < quantity) {
			throw new MenuItemException("Not enough quantity for menu item: " + menuItem.getName());
		}

		// Check if the item already exists in the cart.
		Optional<CartItem> optionalCartItem = cart.getCardItem().stream()
				.filter(item -> item.getMenuItem().getId().equals(menuItemId)).findFirst();

		if (optionalCartItem.isPresent()) {
			CartItem cartItem = optionalCartItem.get();
			int newQty = cartItem.getQuantity() + quantity;
			cartItem.setQuantity(newQty);
			cartItem.setSubTotal(menuItem.getPrice().multiply(BigDecimal.valueOf(newQty)));
		} else {
			CartItem newCartItem = new CartItem();
			newCartItem.setCart(cart);
			newCartItem.setMenuItem(menuItem);
			newCartItem.setQuantity(quantity);
			newCartItem.setSubTotal(menuItem.getPrice().multiply(BigDecimal.valueOf(quantity)));
			cart.getCardItem().add(newCartItem);
		}

		recalcCart(cart);

		menuItem.setQuantity(menuItem.getQuantity() - quantity);
		menuItemRepository.save(menuItem);

		return cartRepository.save(cart);
	}

	private void recalcCart(Cart cart) {
		int totalQty = 0;
		BigDecimal totalAmount = BigDecimal.ZERO;

		for (CartItem item : cart.getCardItem()) {
			totalQty += item.getQuantity();
			totalAmount = totalAmount.add(item.getSubTotal());
		}
		cart.setTotalQty(totalQty);
		cart.setTotalAmount(totalAmount);
	}

	@Override
	public void removeItemFromCart(String userId, Integer cartItemId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserException("User not found with id: " + userId));
		Cart cart = cartRepository.findByUserAndCheckout(user, false)
				.orElseThrow(() -> new RuntimeException("Active cart not found"));

		cart.getCardItem().removeIf(item -> item.getId().equals(cartItemId));
		recalcCart(cart);
		cartRepository.save(cart);

	}

	@Override
	public Cart checkoutCart(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserException("User not found with id: " + userId));
		Cart cart = cartRepository.findByUserAndCheckout(user, false)
				.orElseThrow(() -> new RuntimeException("Active cart not found"));
		cart.setCheckout(true);
		return cartRepository.save(cart);
	}

	@Override
	public Cart updateCartItem(String userId, Integer menuItemId, int newQuantity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cart clearCart(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional(readOnly = true)
	public Cart getActiveCart(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserException("User not found with id: " + userId));

		return cartRepository.findByUserAndCheckout(user, false).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setUser(user);
			newCart.setTotalQty(0);
			newCart.setTotalAmount(BigDecimal.ZERO);
			newCart.setCheckout(false);
			return newCart;
		});
	}

}
