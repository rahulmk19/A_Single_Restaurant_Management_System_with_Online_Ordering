package com.foodtaste.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodtaste.dto.OrderItemRequest;
import com.foodtaste.dto.OrderItemResponse;
import com.foodtaste.dto.OrderRequest;
import com.foodtaste.dto.OrderResponse;
import com.foodtaste.enums.StatusEnum;
import com.foodtaste.exception.MenuItemException;
import com.foodtaste.exception.OrderException;
import com.foodtaste.exception.UserException;
import com.foodtaste.model.MenuItem;
import com.foodtaste.model.OrderDetail;
import com.foodtaste.model.OrderItem;
import com.foodtaste.model.User;
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
	private UserRepo userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Transactional
	@Override
	public OrderResponse createOrder(OrderRequest orderRequest) {

		List<OrderItem> orderItems = new ArrayList<>();
		BigDecimal totalOrderAmount = BigDecimal.ZERO;
		Integer totalOrderQuantity = 0;

		User user = userRepository.findByUsername(JwtFilter.currentUser)
				.orElseThrow(() -> new UserException("User not found with email: " + JwtFilter.currentUser));

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

	@Override
	public OrderResponse getOrderById(Integer orderId) {
		OrderDetail orderDetail = orderDetailRepo.findById(orderId)
				.orElseThrow(() -> new OrderException("Order not found with id: " + orderId));
		OrderResponse orderResponse = modelMapper.map(orderDetail, OrderResponse.class);
		return orderResponse;
	}

	@Override
	public List<OrderDetail> getAllOrders() {
		return orderDetailRepo.findAll();
	}

	@Override
	public OrderDetail updateOrderStatus(Integer id, StatusEnum status) {
		OrderDetail orderDetail = orderDetailRepo.findById(id)
				.orElseThrow(() -> new OrderException("Order not found with id: " + id));
		orderDetail.setStatus(status);
		return orderDetailRepo.save(orderDetail);
	}

	@Override
	public OrderDetail getOrderByIdWithItems(Integer id) {
		return orderDetailRepo.findByIdWithItems(id)
				.orElseThrow(() -> new OrderException("Order not found with id: " + id));
	}

	@Override
	@Transactional
	public OrderDetail cancelOrderById(Integer orderId) {
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
		return orderDetailRepo.save(orderDetail);
	}
}
