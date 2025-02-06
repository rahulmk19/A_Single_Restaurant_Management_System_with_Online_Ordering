package com.foodtaste.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import com.foodtaste.repository.MenuItemRepository;
import com.foodtaste.repository.OrderDetailRepository;
import com.foodtaste.repository.OrderItemRepository;
import com.foodtaste.repository.UserRepository;
import com.foodtaste.service.OrderDetailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderDetailServiceImpl implements OrderDetailService {

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public OrderResponse createOrder(Integer userId, OrderRequest orderRequest) {

		List<OrderItem> orderItems = new ArrayList<>();
		BigDecimal totalAmount = BigDecimal.ZERO;
		Integer totalQuantity = 0;

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserException("User not found with id: " + userId));

		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setCustomerName(user.getName());
		orderDetail.setCustomerPhone(user.getMobileNumber());
		orderDetail.setStatus(StatusEnum.RECEIVED);
		orderDetail.setCreatedAt(LocalDateTime.now());
		orderDetail.setUser(user);

		for (OrderItemRequest itemRequest : orderRequest.getItems()) {
			MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
					.orElseThrow(() -> new MenuItemException("MenuItem not found: " + itemRequest.getMenuItemId()));

			if (menuItem.getQuantity() < itemRequest.getQuantity()) {
				throw new MenuItemException("Not enough quantity for menu item: " + menuItem.getName());
			}

			BigDecimal subTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

			OrderItem orderItem = new OrderItem();
			orderItem.setMenuItem(menuItem);
			orderItem.setQuantity(itemRequest.getQuantity());
			orderItem.setSubTotal(subTotal);
			orderItem.setOrderDetail(orderDetail);

			orderItems.add(orderItem);
			menuItem.setQuantity(menuItem.getQuantity() - itemRequest.getQuantity());
			menuItemRepository.save(menuItem);

			totalQuantity += itemRequest.getQuantity();
			totalAmount = totalAmount.add(subTotal);
		}

		orderDetail.setQuantity(totalQuantity);
		orderDetail.setTotalAmount(totalAmount);
		orderDetailRepository.save(orderDetail);

		orderItems.forEach(item -> item.setOrderDetail(orderDetail));
		orderItemRepository.saveAll(orderItems);

		List<OrderItemResponse> orderItemResponse = orderItems.stream().map(
				item -> new OrderItemResponse(item.getMenuItem().getName(), item.getQuantity(), item.getSubTotal()))
				.toList();
		return new OrderResponse(orderDetail.getId(), totalAmount, totalQuantity, orderDetail.getCreatedAt(),
				orderItemResponse);

	}

	@Override
	public OrderDetail getOrderById(Integer id) {

		OrderDetail orderDetail = orderDetailRepository.findById(id)
				.orElseThrow(() -> new OrderException("Order not found with id:" + id));
		return orderDetail;
	}

	@Override
	public List<OrderDetail> getAllOrders() {
		return orderDetailRepository.findAll();
	}

	@Override
	public OrderDetail updateOrderStatus(Integer id, StatusEnum status) {
		OrderDetail orderDetails = getOrderById(id);
		orderDetails.setStatus(status);
		return orderDetailRepository.save(orderDetails);
	}

	@Override
	public OrderDetail getOrderByIdWithItems(Integer id) {
		OrderDetail orderDetail = orderDetailRepository.findByIdWithItems(id)
				.orElseThrow(() -> new OrderException("Order not found with id:" + id));
		return orderDetail;
	}

	@Override
	@Transactional
	public OrderDetail cancelOrderById(Integer id) {
		OrderDetail orderDetail = getOrderById(id);

		List<OrderItem> allOrderItem = orderDetail.getItems();

		for (OrderItem orderItem : allOrderItem) {

			MenuItem orderedMenuItem = orderItem.getMenuItem();

			MenuItem databaseMenuItem = menuItemRepository.findById(orderedMenuItem.getId())
					.orElseThrow(() -> new MenuItemException("MenuItem id doesn't exits " + orderedMenuItem.getId()));

			databaseMenuItem.setQuantity(databaseMenuItem.getQuantity() + orderItem.getQuantity());
			menuItemRepository.save(databaseMenuItem);
		}
		orderDetail.setStatus(StatusEnum.CANCELED);
		return orderDetailRepository.save(orderDetail);

	}

}
