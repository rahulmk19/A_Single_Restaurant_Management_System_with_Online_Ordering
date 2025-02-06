package com.foodtaste.service;

import java.util.List;

import com.foodtaste.model.OrderItem;

public interface OrderItemService {

	OrderItem addOrderItem(OrderItem orderItem);

	List<OrderItem> getAllOrderItems();

}
