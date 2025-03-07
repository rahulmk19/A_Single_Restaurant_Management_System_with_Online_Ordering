//package com.foodtaste.serviceImpl;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.foodtaste.exception.MenuItemException;
//import com.foodtaste.exception.UserException;
//import com.foodtaste.model.Cart;
//import com.foodtaste.model.CartItem;
//import com.foodtaste.model.MenuItem;
//import com.foodtaste.model.User;
//import com.foodtaste.repository.CartItemRepository;
//import com.foodtaste.repository.CartRepository;
//import com.foodtaste.repository.MenuItemRepository;
//import com.foodtaste.repository.UserRepo;
//import com.foodtaste.repository.UserRepository;
//import com.foodtaste.service.CartService;
//
//@Service
//@Transactional
//public class CartServiceImpl implements CartService {
//
//	@Autowired
//	private CartRepository cartRepository;
//
//	@Autowired
//	private CartItemRepository cartItemRepository;
//
//	@Autowired
//	private UserRepo userRepository;
//
//	@Autowired
//	private MenuItemRepository menuItemRepository;
//
//	public Cart getOrCreateCart(User user) {
//		Cart cart = cartRepository.findByUser(user);
//		if (cart == null) {
//			cart = new Cart();
//			cart.setUser(user);
//			cart = cartRepository.save(cart);
//		}
//		return cart;
//	}
//
//	@Override
//	public Cart addItemToCart(String userId, Integer menuItemId) {
//		User user = userRepository.findById(userId)
//				.orElseThrow(() -> new UserException("User not found with id: " + userId));
//
//		Cart cart = getOrCreateCart(user);
//
//		MenuItem menuItem = menuItemRepository.findById(menuItemId)
//				.orElseThrow(() -> new MenuItemException("MenuItem not found with id: " + menuItemId));
//
//		if (menuItem.getQuantity() <= 0) {
//			throw new MenuItemException("MenuItem is out of stock");
//		}
//
//		// Check if the item already exists in the cart.
//		Optional<CartItem> existingItem = cart.getCardItem().stream()
//				.filter(item -> item.getMenuItem().getId().equals(menuItemId)).findFirst();
//
//		if (!existingItem.isPresent()) {
//			CartItem cartItem = new CartItem();
//			cartItem.setMenuItem(menuItem);
//			cartItem.setQuantity(1);
//			cartItem.setSubTotal(menuItem.getPrice());
//			cart.getCardItem().add(cartItem);
//		}
//		menuItem.setQuantity(menuItem.getQuantity() - 1);
//
//		menuItemRepository.save(menuItem);
//
//		return cartRepository.save(cart);// ?CartItemRepository or cartRepository
//	}
//
//	@Override
//	public void removeItemFromCart(String userId, Integer cartItemId) {
//		User user = userRepository.findById(userId)
//				.orElseThrow(() -> new UserException("User not found with id: " + userId));
//		Cart cart = cartRepository.findByUserAndCheckout(user, false)
//				.orElseThrow(() -> new RuntimeException("Active cart not found"));
//
//		cart.getCardItem().removeIf(item -> item.getId().equals(cartItemId));
//		recalcCart(cart);
//		cartRepository.save(cart);
//
//	}
//
//	@Override
//	public Cart updateCartItemQuantity(String userId, Integer menuItemId, int newQuantity) {
//		User user = userRepository.findById(userId)
//				.orElseThrow(() -> new UserException("User not found with id: " + userId));
//
//		Cart cart = getOrCreateCart(user);
//
//		MenuItem menuItem = menuItemRepository.findById(menuItemId)
//				.orElseThrow(() -> new MenuItemException("MenuItem not found with id: " + menuItemId));
//
//		if (menuItem.getQuantity() < newQuantity) {
//			throw new MenuItemException("MenuItem is out of stock");
//		};
//		
//		
//		
//		Optional<CartItem> existingItem = cart.getCardItem().stream()
//				.filter(item -> item.getMenuItem().getId().equals(menuItemId)).findFirst();
//		
//		if(existingItem.isPresent()) {
//			CartItem cartItem= cart.getCardItem().stream().filter()
//		}
//		
//		if(newQuantity<=0) {
//			cart.getCardItem().remove(cartItem);
//		}
//		else {
//			cartItem.setQuantity(newQuantity);
//		}
//		
//		
//
//		return null;
//	}
//
//	private void recalcCart(Cart cart) {
//		int totalQty = 0;
//		BigDecimal totalAmount = BigDecimal.ZERO;
//
//		for (CartItem item : cart.getCardItem()) {
//			totalQty += item.getQuantity();
//			totalAmount = totalAmount.add(item.getSubTotal());
//		}
//		cart.setTotalQty(totalQty);
//		cart.setTotalAmount(totalAmount);
//	}
//
//	@Override
//	public Cart clearCart(String userId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Transactional(readOnly = true)
//	public Cart getActiveCart(String userId) {
//		User user = userRepository.findById(userId)
//				.orElseThrow(() -> new UserException("User not found with id: " + userId));
//
//		return cartRepository.findByUserAndCheckout(user, false).orElseGet(() -> {
//			Cart newCart = new Cart();
//			newCart.setUser(user);
//			newCart.setTotalQty(0);
//			newCart.setTotalAmount(BigDecimal.ZERO);
//			newCart.setCheckout(false);
//			return newCart;
//		});
//	}
//
//}
