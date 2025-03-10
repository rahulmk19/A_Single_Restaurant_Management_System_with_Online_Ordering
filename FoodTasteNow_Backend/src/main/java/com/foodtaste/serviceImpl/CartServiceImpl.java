package com.foodtaste.serviceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodtaste.dto.CartResponse;
import com.foodtaste.dto.OrderResponse;
import com.foodtaste.exception.MenuItemException;
import com.foodtaste.exception.UserException;
import com.foodtaste.model.Cart;
import com.foodtaste.model.CartItem;
import com.foodtaste.model.MenuItem;
import com.foodtaste.model.User;
import com.foodtaste.repository.CartItemRepo;
import com.foodtaste.repository.CartRepo;
import com.foodtaste.repository.MenuItemRepo;
import com.foodtaste.repository.UserRepo;
import com.foodtaste.service.CartService;
import com.foodtaste.service.OrderDetailService;

@Service
@Transactional
public class CartServiceImpl implements CartService {

	private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private CartItemRepo cartItemRepo;

	@Autowired
	private UserRepo userRepository;

	@Autowired
	private MenuItemRepo menuItemRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private OrderDetailService orderDetailService;

	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			log.error("Unauthenticated access attempt");
			throw new UserException("User is not authenticated");
		}
		String username = authentication.getName();
		return userRepository.findByUsername(username).orElseThrow(() -> {
			log.error("User not found with username: {}", username);
			return new UserException("User not found with username: " + username);
		});
	}

	@Override
	public CartResponse getCart() {
		Cart cart = getOrCreateCart(getCurrentUser());
		CartResponse response = modelMapper.map(cart, CartResponse.class);
		return response;

	}

	private Cart getOrCreateCart(User user) {
		return cartRepo.findByUser(user).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setUser(user);
			log.info("Created new cart for user: {}", user.getUsername());
			return cartRepo.save(newCart);
		});
	}

	@Transactional
	@Override
	public CartResponse addItemToCart(Integer menuItemId) {
		User user = getCurrentUser();
		Cart cart = getOrCreateCart(user);

		MenuItem menuItem = menuItemRepo.findById(menuItemId)
				.orElseThrow(() -> new MenuItemException("MenuItem not found with id: " + menuItemId));

		if (menuItem.getQuantity() <= 0) {
			log.error("MenuItem {} is out of stock", menuItem.getName());
			throw new MenuItemException("MenuItem is out of stock");
		}

		Optional<CartItem> existingItemOpt = cart.getCartItem().stream()
				.filter(item -> item.getMenuItem().getId().equals(menuItemId)).findFirst();

		if (existingItemOpt.isPresent()) {
			CartItem existingItem = existingItemOpt.get();
			existingItem.setQuantity(existingItem.getQuantity() + 1);
			existingItem.setSubTotal(menuItem.getPrice().multiply(new BigDecimal(existingItem.getQuantity())));
			cartItemRepo.save(existingItem);
			log.info("Incremented quantity of MenuItem {} in cart", menuItem.getName());
		} else {
			CartItem newItem = new CartItem();
			newItem.setMenuItem(menuItem);
			newItem.setQuantity(1);
			newItem.setSubTotal(menuItem.getPrice());
			newItem.setCart(cart);
			cart.getCartItem().add(newItem);
			cartItemRepo.save(newItem);
			log.info("Added new MenuItem {} to cart", menuItem.getName());
		}

		menuItem.setQuantity(menuItem.getQuantity() - 1);
		menuItemRepo.save(menuItem);
		log.info("Updated stock for MenuItem {}. New stock: {}", menuItem.getName(), menuItem.getQuantity());

		recalculateCart(cart);
		Cart savedCart = cartRepo.save(cart);
		CartResponse cartResponse = modelMapper.map(savedCart, CartResponse.class);
		return cartResponse;
	}

	@Transactional
	@Override
	public CartResponse removeItemFromCart(Integer menuItemId) {
		User user = getCurrentUser();
		Cart cart = cartRepo.findByUser(user)
				.orElseThrow(() -> new UserException("Cart not found for user: " + user.getUsername()));

		Optional<CartItem> removedItemOpt = cart.getCartItem().stream()
				.filter(item -> item.getMenuItem().getId().equals(menuItemId)).findFirst();

		if (removedItemOpt.isPresent()) {
			CartItem removedItem = removedItemOpt.get();
			cart.getCartItem().remove(removedItem);
			log.info("Removed MenuItem {} from cart", removedItem.getMenuItem().getName());

			MenuItem menuItem = removedItem.getMenuItem();
			menuItem.setQuantity(menuItem.getQuantity() + removedItem.getQuantity());
			menuItemRepo.save(menuItem);
		} else {
			log.error("Cart item with MenuItem id {} not found in cart", menuItemId);
			throw new MenuItemException("Item not found in the cart with id: " + menuItemId);
		}
		recalculateCart(cart);
		Cart savedCart = cartRepo.save(cart);
		CartResponse cartResponse = modelMapper.map(savedCart, CartResponse.class);
		return cartResponse;
	}

	@Transactional
	@Override
	public CartResponse updateCartItemQuantity(Integer menuItemId, int newQuantity) {
		User user = getCurrentUser();
		Cart cart = getOrCreateCart(user);

		MenuItem menuItem = menuItemRepo.findById(menuItemId)
				.orElseThrow(() -> new MenuItemException("MenuItem not found with id: " + menuItemId));

		if (menuItem.getQuantity() < newQuantity) {
			log.error("Insufficient stock for MenuItem {}: requested {}, available {}", menuItem.getName(), newQuantity,
					menuItem.getQuantity());
			throw new MenuItemException("Insufficient stock for the requested quantity");
		}

		Optional<CartItem> existingItemOpt = cart.getCartItem().stream()
				.filter(item -> item.getMenuItem().getId().equals(menuItemId)).findFirst();

		if (existingItemOpt.isPresent()) {
			CartItem cartItem = existingItemOpt.get();

			if (newQuantity <= 0) {
				cart.getCartItem().remove(cartItem);
				menuItem.setQuantity(menuItem.getQuantity() + cartItem.getQuantity());
				menuItemRepo.save(menuItem);
				log.info("Removed MenuItem {} from cart due to zero quantity", menuItem.getName());
			} else {

				int quantityDifference = newQuantity - cartItem.getQuantity();
				menuItem.setQuantity(menuItem.getQuantity() - quantityDifference);
				menuItemRepo.save(menuItem);

				cartItem.setQuantity(newQuantity);
				cartItem.setSubTotal(menuItem.getPrice().multiply(new BigDecimal(newQuantity)));
				cartItemRepo.save(cartItem);
				log.info("Updated quantity of MenuItem {} to {}", menuItem.getName(), newQuantity);
			}
		} else {
			log.error("Cart item not found for MenuItem id: {}", menuItemId);
			throw new MenuItemException("Cart item not found with menuItem id: " + menuItemId);
		}
		recalculateCart(cart);
		Cart savedCart = cartRepo.save(cart);
		CartResponse cartResponse = modelMapper.map(savedCart, CartResponse.class);
		return cartResponse;
	}

	public OrderResponse placedOrder() {
		return orderDetailService.checkout();
	}

	private void recalculateCart(Cart cart) {
		int totalQty = 0;
		BigDecimal totalAmount = BigDecimal.ZERO;
		for (CartItem item : cart.getCartItem()) {
			totalQty += item.getQuantity();
			totalAmount = totalAmount.add(item.getSubTotal());
		}
		cart.setTotalQty(totalQty);
		cart.setTotalAmount(totalAmount);
		log.info("Recalculated cart totals: totalQty={}, totalAmount={}", totalQty, totalAmount);
	}
}
