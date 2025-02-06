package com.foodtaste.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodtaste.model.MenuItem;
import com.foodtaste.model.OrderItem;
import com.foodtaste.repository.OrderItemRepository;
import com.foodtaste.service.MenuItemService;
import com.foodtaste.service.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private MenuItemService menuItemService;

	@Override
	public OrderItem addOrderItem(OrderItem orderItem) {
		MenuItem menuItem = menuItemService.getItemById(orderItem.getMenuItem().getId());
		
		return null;
	}

	@Override
	public List<OrderItem> getAllOrderItems() {
		return orderItemRepository.findAll();
	}

}
