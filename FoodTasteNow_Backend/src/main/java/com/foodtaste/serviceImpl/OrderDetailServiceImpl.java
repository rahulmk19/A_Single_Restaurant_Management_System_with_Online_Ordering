package com.foodtaste.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodtaste.dto.OrderItemRequest;
import com.foodtaste.dto.OrderItemResponse;
import com.foodtaste.dto.OrderRequest;
import com.foodtaste.dto.OrderResponse;
import com.foodtaste.enums.StatusEnum;
import com.foodtaste.exception.CartException;
import com.foodtaste.exception.MenuItemException;
import com.foodtaste.exception.OrderException;
import com.foodtaste.exception.UserException;
import com.foodtaste.model.Cart;
import com.foodtaste.model.CartItem;
import com.foodtaste.model.MenuItem;
import com.foodtaste.model.OrderDetail;
import com.foodtaste.model.OrderItem;
import com.foodtaste.model.User;
import com.foodtaste.repository.CartRepo;
import com.foodtaste.repository.MenuItemRepo;
import com.foodtaste.repository.OrderDetailRepo;
import com.foodtaste.repository.OrderItemRepo;
import com.foodtaste.repository.UserRepo;
import com.foodtaste.security.jwt.JwtFilter;
import com.foodtaste.service.OrderDetailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderDetailServiceImpl implements OrderDetailService {

	@Autowired
	private OrderDetailRepo orderDetailRepo;

	@Autowired
	private MenuItemRepo menuItemRepo;

	@Autowired
	private OrderItemRepo orderItemRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private ModelMapper modelMapper;

	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated()) {
			throw new UserException("User is not authenticated");
		}
		String username = auth.getName();
		return userRepo.findByUsername(username)
				.orElseThrow(() -> new UserException("User not found with username: " + username));
	}

	@Transactional
	@Override
	public OrderResponse createOrder(OrderRequest orderRequest) {

		User user = getCurrentUser();
		List<OrderItem> orderItems = new ArrayList<>();
		BigDecimal totalOrderAmount = BigDecimal.ZERO;
		Integer totalOrderQuantity = 0;

		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setCustomerName(orderRequest.getCustomerName());
		orderDetail.setContactNum(orderRequest.getContactNum());
		orderDetail.setAlternateContactNum(orderRequest.getAlternateContactNum());
		orderDetail.setCustomerAddress(orderRequest.getCustomerAddress());
		orderDetail.setStatus(StatusEnum.PENDING);
		orderDetail.setCreatedAtTime(LocalDateTime.now());
		orderDetail.setUser(user);

		for (OrderItemRequest itemRequest : orderRequest.getItems()) {
			MenuItem menuItem = menuItemRepo.findById(itemRequest.getMenuItemId())
					.orElseThrow(() -> new MenuItemException("MenuItem not found: " + itemRequest.getMenuItemId()));

			if (menuItem.getQuantity() < itemRequest.getQuantity()) {
				throw new MenuItemException(menuItem.getName() + " available quantity is " + menuItem.getQuantity());
			}

			BigDecimal subTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

			OrderItem orderItem = new OrderItem();
			orderItem.setMenuItem(menuItem);
			orderItem.setQuantity(itemRequest.getQuantity());
			orderItem.setSubTotal(subTotal);
			orderItem.setOrderDetail(orderDetail);

			orderItems.add(orderItem);
			menuItem.setQuantity(menuItem.getQuantity() - itemRequest.getQuantity());
			menuItemRepo.save(menuItem);

			totalOrderQuantity += itemRequest.getQuantity();
			totalOrderAmount = totalOrderAmount.add(subTotal);
		}

		orderDetail.setTotalOrderQuantity(totalOrderQuantity);
		orderDetail.setTotalOrderAmount(totalOrderAmount);
		orderDetailRepo.save(orderDetail);

		orderItems.forEach(item -> item.setOrderDetail(orderDetail));
		orderItemRepo.saveAll(orderItems);

		List<OrderItemResponse> orderItemResponse = orderItems.stream().map(
				item -> new OrderItemResponse(item.getMenuItem().getName(), item.getQuantity(), item.getSubTotal()))
				.toList();

		return new OrderResponse(orderDetail.getId(), user.getId(), totalOrderAmount, totalOrderQuantity,
				orderDetail.getCreatedAtTime(), orderDetail.getCustomerName(), orderDetail.getCustomerAddress(),
				orderDetail.getContactNum(), orderDetail.getAlternateContactNum(), orderDetail.getStatus(),
				orderItemResponse);
	}

	@Transactional
	@Override
	public OrderResponse checkout() {
		User user = getCurrentUser();
		Cart cart = cartRepo.findByUser(user)
				.orElseThrow(() -> new CartException("Cart not found for user: " + user.getUsername()));

		if (cart.getCartItem().isEmpty()) {
			throw new CartException("Cannot checkout an empty cart");
		}

		OrderDetail order = new OrderDetail();
		order.setUser(user);
		order.setTotalOrderAmount(cart.getTotalAmount());
		order.setTotalOrderQuantity(cart.getTotalQty());
		order.setStatus(StatusEnum.PENDING);
		order.setCreatedAtTime(LocalDateTime.now());

		List<OrderItem> orderItems = new ArrayList<>();
		for (CartItem cartItem : cart.getCartItem()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setMenuItem(cartItem.getMenuItem());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSubTotal(cartItem.getSubTotal());
			orderItem.setOrderDetail(order);
			orderItems.add(orderItem);
		}
		order.setItems(orderItems);

		OrderDetail savedOrder = orderDetailRepo.save(order);

		cart.getCartItem().clear();
		cartRepo.save(cart);

		OrderResponse orderPlaced = modelMapper.map(savedOrder, OrderResponse.class);
		return orderPlaced;
	}

	@Override
	public OrderResponse getOrderById(Integer orderId) {
		OrderDetail orderDetail = orderDetailRepo.findById(orderId)
				.orElseThrow(() -> new OrderException("Order not found with id: " + orderId));

		OrderResponse orderResponse = modelMapper.map(orderDetail, OrderResponse.class);
		return orderResponse;
	}

	@Override
	public List<OrderResponse> getAllOrders() {
		List<OrderDetail> allOrders = orderDetailRepo.findAll();
		List<OrderResponse> orderResponse = allOrders.stream()
				.map(orderDetail -> modelMapper.map(orderDetail, OrderResponse.class)).toList();
		return orderResponse;
	}

	@Override
	public OrderResponse updateOrderStatus(Integer userId, StatusEnum status) {
		OrderDetail orderDetail = orderDetailRepo.findById(userId)
				.orElseThrow(() -> new OrderException("Order not found with id: " + userId));
		orderDetail.setStatus(status);
		orderDetail = orderDetailRepo.save(orderDetail);
		return modelMapper.map(orderDetail, OrderResponse.class);
	}

	@Override
	public OrderDetail getOrderByIdWithItems(Integer id) {
		return orderDetailRepo.findByIdWithItems(id)
				.orElseThrow(() -> new OrderException("Order not found with id: " + id));
	}

	@Override
	@Transactional
	public OrderResponse cancelOrderById(Integer orderId) {
		OrderDetail orderDetail = orderDetailRepo.findById(orderId)
				.orElseThrow(() -> new OrderException("Order not found with id: " + orderId));
		List<OrderItem> allOrderItem = orderDetail.getItems();

		for (OrderItem orderItem : allOrderItem) {
			MenuItem databaseMenuItem = menuItemRepo.findById(orderItem.getMenuItem().getId()).orElseThrow(
					() -> new MenuItemException("MenuItem not found with id: " + orderItem.getMenuItem().getId()));
			databaseMenuItem.setQuantity(databaseMenuItem.getQuantity() + orderItem.getQuantity());
			menuItemRepo.save(databaseMenuItem);
		}
		orderDetail.setStatus(StatusEnum.CANCELED);
		orderDetail = orderDetailRepo.save(orderDetail);
		return modelMapper.map(orderDetail, OrderResponse.class);
	}

	@Override
	public List<OrderResponse> getOrdersByUser() {
		User user = getCurrentUser();
		Long userId = userRepo.findByUsername(user.getUsername()).get().getId();
		List<OrderDetail> allOrders = orderDetailRepo.findOrdersByUserId(userId);
		List<OrderResponse> getAllOrderResponse = allOrders.stream()
				.map(order -> modelMapper.map(order, OrderResponse.class)).toList();

		return getAllOrderResponse;
	}
}
